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
import android.widget.Toast
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
    private lateinit var connectRescanButton: Button
    private lateinit var sendPairingCodeButton: Button
    private lateinit var rescanButton: Button
    private lateinit var firstPairingCode : EditText
    private lateinit var secondPairingCode : EditText
    private lateinit var thirdPairingCode : EditText
    private lateinit var fourthPairingCode : EditText

    private var mirrorSelectedDisposable: Disposable? = null
    private val mirrorSelected : PublishSubject<Int> = PublishSubject.create()
    val onMirrorSelected: Observable<Int> = mirrorSelected

    private val connectButtonClicked : PublishSubject<Boolean> = PublishSubject.create()
    val onConnectButtonClicked: Observable<Boolean> = connectButtonClicked

    private val rescanButtonClicked : PublishSubject<Boolean> = PublishSubject.create()
    val onRescanButtonClicked: Observable<Boolean> = rescanButtonClicked

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
        connectRescanButton = rootXml.findViewById(R.id.connect)
        sendPairingCodeButton.setOnClickListener { sendPairingButtonClicked.onNext(buildPairingCode()) }
        setupPairingCodeInputs()
        toggleConnectRescanButton(true)
    }

    fun toggleConnectRescanButton(rescanEnabled : Boolean) {
        if (rescanEnabled) {
            connectRescanButton.setText(R.string.rescan)
            connectRescanButton.setOnClickListener { rescanButtonClicked.onNext(true) }
        } else {
            connectRescanButton.setText(R.string.connect)
            connectRescanButton.setOnClickListener { connectButtonClicked.onNext(true) }
        }
    }
    private fun buildPairingCode(): String {
        return "${firstPairingCode.text}${secondPairingCode.text}${thirdPairingCode.text}${fourthPairingCode.text}"
    }

    private fun setupPairingCodeInputs() {
        val pairingInputs = listOf( firstPairingCode, secondPairingCode, thirdPairingCode, fourthPairingCode)
        val enablePairingCodeButton = buildPairingCodeTextWatcher(pairingInputs)
        val pairingInputFocusListener = buildPairingCodeFocusListener(pairingInputs)
        pairingInputs.forEach { it.addTextChangedListener(enablePairingCodeButton) }
        pairingInputs.forEach { it.setOnFocusChangeListener(pairingInputFocusListener) }
    }

    private fun buildPairingCodeFocusListener(pairingInputs: List<EditText>): View.OnFocusChangeListener {
        return object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View, hasFocus: Boolean) {
                if (!hasFocus) return
                val nextInput = getNextPairingInput(pairingInputs)
                if (nextInput != -1) {
                    val isEmpty = (v as EditText).text.isEmpty()
                    if (pairingInputs[nextInput] != v && isEmpty) {
                        pairingInputs[nextInput].requestFocus()
                    }
                }
            }

        }
    }

    private fun buildPairingCodeTextWatcher(pairingInputs: List<EditText>): TextWatcher {
        return object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val emptyCodes = pairingInputs.map { it.text.toString().isEmpty() }
                sendPairingCodeButton.isEnabled = emptyCodes.none { it }

                val nextInput = getNextPairingInput(pairingInputs)
                if (nextInput != -1) {
                    pairingInputs[nextInput].requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

    private fun getNextPairingInput(pairingInputs: List<EditText>): Int {
        return pairingInputs.indexOfFirst { it.text.toString().isEmpty() }
    }

    fun displayLoadingScreen() {
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

    fun displayMirrorsScreen(serviceNames: List<String>) {
        val adapter = SelectMirrorAdapter(serviceNames)
        mirrorSelectedDisposable = adapter.onMirrorSelected.subscribe{ mirrorSelected.onNext(it) }
        runOnUiThread {
            mirrorList.layoutManager = LinearLayoutManager(rootXml.context, LinearLayoutManager.HORIZONTAL, false)
            mirrorList.adapter = adapter
            display(selectMirrorRoot)
        }
    }

    fun unsubscribe() {
        mirrorSelectedDisposable?.dispose()
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

    fun showPairingError() {
        Toast.makeText(rootXml.context, R.string.invalid_pairing_code, Toast.LENGTH_SHORT).show()
    }
}
