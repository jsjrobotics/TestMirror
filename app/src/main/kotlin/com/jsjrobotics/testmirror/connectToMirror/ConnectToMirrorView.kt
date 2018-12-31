package com.jsjrobotics.testmirror.connectToMirror

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.jsjrobotics.testmirror.DefaultView
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.runOnUiThread
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ConnectToMirrorView @Inject constructor() : DefaultView(){
    override fun getContext(): Context {
        return rootXml.context
    }

    lateinit var rootXml: ViewGroup ; private set

    private lateinit var mirrorList: RecyclerView

    private lateinit var loadingRoot: ViewGroup
    private lateinit var selectMirrorRoot: ViewGroup
    private lateinit var pairingInputRoot: ViewGroup
    private lateinit var connectedRoot: ViewGroup
    private lateinit var loadingMessage: TextView
    private lateinit var connectButton: Button
    private lateinit var sendPairingCodeButton: Button

    private lateinit var firstPairingCode : EditText
    private lateinit var secondPairingCode : EditText
    private lateinit var thirdPairingCode : EditText
    private lateinit var fourthPairingCode : EditText

    private var mirrorSelectedDisposable: Disposable? = null
    private val mirrorSelected : PublishSubject<Int> = PublishSubject.create()
    val onMirrorSelected: Observable<Int> = mirrorSelected

    private val connectButtonClicked : PublishSubject<Boolean> = PublishSubject.create()
    val onConnectButtonClicked: Observable<Boolean> = connectButtonClicked

    private val sendPairingButtonClicked : PublishSubject<String> = PublishSubject.create()
    val onSendPairingButtonClicked: Observable<String> = sendPairingButtonClicked


    fun init(inflater: LayoutInflater, container: ViewGroup) {
        rootXml = inflater.inflate(R.layout.fragment_connect_to_mirror, container, false) as ViewGroup
        loadingRoot = rootXml.findViewById(R.id.loading)
        loadingMessage = loadingRoot.findViewById(R.id.loading_message)
        pairingInputRoot = rootXml.findViewById(R.id.pairing_input)
        sendPairingCodeButton = pairingInputRoot.findViewById(R.id.send_pairing_code)
        firstPairingCode = pairingInputRoot.findViewById(R.id.first_digit)
        secondPairingCode = pairingInputRoot.findViewById(R.id.second_digit)
        thirdPairingCode = pairingInputRoot.findViewById(R.id.third_digit)
        fourthPairingCode = pairingInputRoot.findViewById(R.id.fourth_digit)

        selectMirrorRoot = rootXml.findViewById(R.id.select_mirror)
        connectedRoot = rootXml.findViewById(R.id.connected)
        mirrorList = rootXml.findViewById(R.id.mirror_list)
        connectButton = rootXml.findViewById(R.id.connect)
        connectButton.setOnClickListener { connectButtonClicked.onNext(true) }
        connectButton.setText(R.string.connect)
        sendPairingCodeButton.setOnClickListener { sendPairingButtonClicked.onNext(buildPairingCode()) }
        setupPairingCodeInputs()
        disableConnectButton()
        displayLoading()
    }

    private fun buildPairingCode(): String {
        return "${firstPairingCode.text}${secondPairingCode.text}${thirdPairingCode.text}${fourthPairingCode.text}"
    }

    private fun setupPairingCodeInputs() {
        val pairingInputs = listOf( firstPairingCode, secondPairingCode, thirdPairingCode, fourthPairingCode)
        val enablePairingCodeButton = object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val emptyCodes = pairingInputs.map { it.text.toString().isEmpty() }
                sendPairingCodeButton.isEnabled = emptyCodes.none { it }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        pairingInputs.forEach { it.addTextChangedListener(enablePairingCodeButton) }
    }

    private fun displayLoading() {
        loadingMessage.text = getContext().getString(R.string.searching_for_mirrors)
        display(loadingRoot)
    }

    private fun display(toDisplay: ViewGroup) {
        runOnUiThread {
            arrayListOf(
                    loadingRoot,
                    selectMirrorRoot,
                    connectedRoot,
                    pairingInputRoot
            ).forEach {
                if (it == toDisplay) {
                    it.visibility = View.VISIBLE
                } else {
                    it.visibility = View.GONE
                }
            }
        }
    }

    fun displayMirrors(serviceNames: List<String>) {
        val adapter = SelectMirrorAdapter(serviceNames)
        mirrorSelectedDisposable = adapter.onMirrorSelected.subscribe{ mirrorSelected.onNext(it) }
        runOnUiThread {
            mirrorList.layoutManager = LinearLayoutManager(rootXml.context, LinearLayoutManager.HORIZONTAL, false)
            mirrorList.adapter = adapter
            display(selectMirrorRoot)
        }
    }

    fun onDestroy() {
        mirrorSelectedDisposable?.dispose()
    }

    fun setMirrorSelected(selectedMirror: Int) {
        0.until(mirrorList.childCount)
                .map { index  -> mirrorList.findViewHolderForAdapterPosition(index) as SelectMirrorViewHolder }
                .forEachIndexed { viewHolderIndex, viewHolder ->
                    if (viewHolderIndex == selectedMirror) {
                        viewHolder.setSelected()
                    } else {
                        viewHolder.setUnselected()
                    }
                }

    }

    fun enableConnectButton() {
        connectButton.visibility = View.VISIBLE
    }

    fun disableConnectButton() {
        connectButton.visibility = View.GONE
    }

    fun unselectMirror(index: Int) {
        (mirrorList.findViewHolderForAdapterPosition(index) as SelectMirrorViewHolder).setUnselected()
    }

    fun showConnecting(mirrorName: String) {
        loadingMessage.text = getContext().getString(R.string.connecting_to, mirrorName)
        display(loadingRoot)
    }

    fun showPairingInput() {
        runOnUiThread {
            sendPairingCodeButton.isEnabled = false
            display(pairingInputRoot)
        }
    }
}
