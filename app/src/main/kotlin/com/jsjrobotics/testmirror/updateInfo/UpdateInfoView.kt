package com.jsjrobotics.testmirror.updateInfo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import com.jsjrobotics.testmirror.DefaultView
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.dataStructures.UpdateInfoData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class UpdateInfoView @Inject constructor() : DefaultView() {
    override fun getContext(): Context = rootXml.context

    lateinit var rootXml: ViewGroup; private set
    lateinit var birthdayInput: DatePicker; private set
    lateinit var locationInput: EditText; private set
    lateinit var saveButton: Button; private set

    private val onSaveClick : PublishSubject<UpdateInfoData> = PublishSubject.create()

    fun init(inflater: LayoutInflater, container: ViewGroup) {
        rootXml = inflater.inflate(R.layout.fragment_update_info, container, false) as ViewGroup
        birthdayInput = rootXml.findViewById(R.id.birthday)
        locationInput = rootXml.findViewById(R.id.location)
        saveButton = rootXml.findViewById(R.id.save)
        saveButton.setOnClickListener {
            val updateInfoData = UpdateInfoData(birthdayInput.year,
                                                birthdayInput.month,
                                                birthdayInput.dayOfMonth,
                                                locationInput.text.toString())
            onSaveClick.onNext(updateInfoData)
        }
    }

    fun onSaveClick(): Observable<UpdateInfoData> = onSaveClick
}