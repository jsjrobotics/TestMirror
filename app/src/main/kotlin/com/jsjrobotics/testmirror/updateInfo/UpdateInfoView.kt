package com.jsjrobotics.testmirror.updateInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import com.jsjrobotics.testmirror.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class UpdateInfoView @Inject constructor() {
    lateinit var rootXml: ViewGroup; private set
    lateinit var birthdayInput: DatePicker; private set
    lateinit var locationInput: EditText; private set
    lateinit var saveButton: Button; private set

    private val onSaveClick : PublishSubject<Unit> = PublishSubject.create()

    fun init(inflater: LayoutInflater, container: ViewGroup) {
        rootXml = inflater.inflate(R.layout.fragment_update_info, container, false) as ViewGroup
        birthdayInput = rootXml.findViewById(R.id.birthday)
        locationInput = rootXml.findViewById(R.id.location)
        saveButton = rootXml.findViewById(R.id.save)
        saveButton.setOnClickListener { onSaveClick.onNext(Unit) }
    }

    fun onSaveClick(): Observable<Unit> = onSaveClick
}