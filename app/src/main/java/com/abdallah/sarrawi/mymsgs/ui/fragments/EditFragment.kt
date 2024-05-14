package com.abdallah.sarrawi.mymsgs.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.ClipboardManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.abdallah.sarrawi.mymsgs.R


class EditFragment : Fragment() {

     var MsgTypes_name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MsgTypes_name = EditFragmentArgs.fromBundle(requireArguments()).msgname
//        MsgTypes_name = EditFragmentArgs.fromBundle(requireArguments()).a
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Toast.makeText(requireContext(), MsgTypes_name, Toast.LENGTH_LONG).show()
        val editText = view.findViewById<EditText>(R.id.intext)

        editText.setText(MsgTypes_name)
        val out1 = view.findViewById<View>(R.id.out1) as EditText
        val out2 = view.findViewById<View>(R.id.out2) as EditText
        val out3 = view.findViewById<View>(R.id.out3) as EditText
        val out4 = view.findViewById<View>(R.id.out4) as EditText
        val out5 = view.findViewById<View>(R.id.out5) as EditText
        val out6 = view.findViewById<View>(R.id.out6) as EditText
        val out7 = view.findViewById<View>(R.id.out7) as EditText
        val out8 = view.findViewById<View>(R.id.out8) as EditText
        val out9 = view.findViewById<View>(R.id.out9) as EditText
        val press = view.findViewById<View>(R.id.press) as ImageView
        val copy1 = view.findViewById<View>(R.id.copy1) as ImageView
        val copy2 = view.findViewById<View>(R.id.copy2) as ImageView
        val copy3 = view.findViewById<View>(R.id.copy3) as ImageView
        val copy4 = view.findViewById<View>(R.id.copy4) as ImageView
        val copy5 = view.findViewById<View>(R.id.copy5) as ImageView
        val copy6 = view.findViewById<View>(R.id.copy6) as ImageView
        val copy7 = view.findViewById<View>(R.id.copy7) as ImageView
        val copy8 = view.findViewById<View>(R.id.copy8) as ImageView
        val copy9 = view.findViewById<View>(R.id.copy9) as ImageView
        val share1 =view. findViewById<View>(R.id.share1) as ImageView
        val share2 =view. findViewById<View>(R.id.share2) as ImageView
        val share3 =view. findViewById<View>(R.id.share3) as ImageView
        val share4 =view. findViewById<View>(R.id.share4) as ImageView
        val share5 =view. findViewById<View>(R.id.share5) as ImageView
        val share6 =view. findViewById<View>(R.id.share6) as ImageView
        val share7 =view. findViewById<View>(R.id.share7) as ImageView
        val share8 =view. findViewById<View>(R.id.share8) as ImageView
        val share9 =view. findViewById<View>(R.id.share9) as ImageView


        var img = view.findViewById<View>(R.id.imageView1) as ImageView
        press.setOnClickListener(View.OnClickListener {
            var j: Int
//            EditFragment.hideSoftKeyboard()
            var o0: String = editText.getText().toString()
            var o3: String = editText.getText().toString()
            var o4: String = editText.getText().toString()
            var o5: String = editText.getText().toString()
            var o6: String = editText.getText().toString()
            var o7: String = editText.getText().toString()
            var o8: String = editText.getText().toString()
            var o9: String = editText.getText().toString()
            var org: String = editText.getText().toString()
            var r19 = CharArray(31)
            r19 = charArrayOf(
                '\u0627',
                '\u0623',
                '\u0625',
                '\u0622',
                '\u0628',
                '\u062a',
                '\u062b',
                '\u062c',
                '\u062d',
                '\u062e',
                '\u062f',
                '\u0630',
                '\u0631',
                '\u0632',
                '\u0633',
                '\u0634',
                '\u0635',
                '\u0636',
                '\u0637',
                '\u0638',
                '\u0639',
                '\u063a',
                '\u0641',
                '\u0642',
                '\u0643',
                '\u0644',
                '\u0645',
                '\u0646',
                '\u0647',
                '\u0648',
                '\u064a'
            )
            val str2 = arrayOf(
                "\u0622",
                "\u0622",
                "\u0622",
                "\u0622",
                "\u0628\u0651\u0640",
                "\u062a\u064f",
                "\u062b\u064b\u0640",
                "\u062c\u064e \u0640",
                "\u062d\u064b \u0640",
                "\u062e\u064c \u0640",
                "\u062f\u064f",
                "\u0630\u064c",
                "\u0631\u064e",
                "\u0632\u064e",
                "\u0633\u064b\u0640",
                "\u0634\u0651\u0640",
                "\u0635\u0650\u0640",
                "\u0636\u0640",
                "\u0637\u064c\u0640",
                "\u0638\u064c\u0640",
                "\u0639\u064e \u0640",
                "\u063a\u0651 \u0640",
                "\u0641\u064f",
                "\u0642\u064e",
                "\u0643\u064e",
                "\u0644\u064e",
                "\u0645\u0650\u0640",
                "\u0646\u064c",
                "\u0647\u0650",
                "\u0648\u064f",
                "\u064a\u0651"
            )
            var r20 = CharArray(31)
            r20 = charArrayOf(
                '\u0627',
                '\u0623',
                '\u0625',
                '\u0622',
                '\u0628',
                '\u062a',
                '\u062b',
                '\u062c',
                '\u062d',
                '\u062e',
                '\u062f',
                '\u0630',
                '\u0631',
                '\u0632',
                '\u0633',
                '\u0634',
                '\u0635',
                '\u0636',
                '\u0637',
                '\u0638',
                '\u0639',
                '\u063a',
                '\u0641',
                '\u0642',
                '\u0643',
                '\u0644',
                '\u0645',
                '\u0646',
                '\u0647',
                '\u0648',
                '\u064a'
            )
            val str22 = arrayOf(
                "\u0622",
                "\u0622",
                "\u0622",
                "\u0622",
                "\u0628\u0651\u06d2\u0640",
                "\u062a\u064f\u06d2\u0640",
                "\u062b\u064b\u06d2\u0640",
                "\u062c\u064e\u06d2\u0640",
                "\u062d\u064b\u06d2\u0640",
                "\u062e\u064c\u06d2\u0640",
                "\u062f\u064f",
                "\u0630\u064c",
                "\u0631\u064e",
                "\u0632\u064e",
                "\u0633\u064b\u06d2\u0640",
                "\u0634\u0651\u06d2\u0640",
                "\u0635\u0650\u06d2\u0640",
                "\u0636\u06d2\u0640",
                "\u0637\u064c\u06d2\u0640",
                "\u0638\u064c\u06d2\u0640",
                "\u0639\u064e\u06d2\u0640",
                "\u063a\u0651\u06d2\u0640",
                "\u0641\u064f\u06d2\u0640",
                "\u0642\u064e\u06d2\u0640",
                "\u0643\u06d2\u0640",
                "\u0644\u064e",
                "\u0645\u0650\u06d2\u0640",
                "\u0646\u064c\u06d2\u0640",
                "\u0647\u06d2\u0640\u0650",
                "\u0648\u064f",
                "\u064a\u0651\u06d2\u0640"
            )
            var r21 = CharArray(32)
            r21 = charArrayOf(
                '\u0627',
                '\u0623',
                '\u0625',
                '\u0622',
                '\u0628',
                '\u062a',
                '\u062b',
                '\u062c',
                '\u062d',
                '\u062e',
                '\u062f',
                '\u0630',
                '\u0631',
                '\u0632',
                '\u0633',
                '\u0634',
                '\u0635',
                '\u0636',
                '\u0637',
                '\u0638',
                '\u0639',
                '\u063a',
                '\u0641',
                '\u0642',
                '\u0643',
                '\u0644',
                '\u0645',
                '\u0646',
                '\u0647',
                '\u0648',
                '\u064a',
                '\u0629'
            )
            val str23 = arrayOf(
                "\u0622",
                "\u0623\u0650",
                "\u064f\u0625",
                "\u0622",
                "\u0628\u06c1",
                "\u062a\u06c1",
                "\u062b\u06c1",
                "\u062c\u06c1",
                "\u062d\u06c1",
                "\u062e\u06c1",
                "\u062f",
                "\u0630",
                "\u0631",
                "\u0632",
                "\u0633\u06c1",
                "\u0634\u06c1",
                "\u0635\u06c1",
                "\u0636\u06c1",
                "\u0637\u06c1",
                "\u0638\u06c1",
                "\u0639\u06c1",
                "\u063a\u06c1",
                "\u0641\u06c1",
                "\u0642\u06c1",
                "\u0643\u06c1",
                "\u0644",
                "\u0645\u06c1",
                "\u0646\u06c1",
                "\u0647\u06c1",
                "\u0624",
                "\u064a\u06c1",
                "\u0647\u06c1"
            )
            var r22 = CharArray(32)
            r22 = charArrayOf(
                '\u0627',
                '\u0623',
                '\u0625',
                '\u0622',
                '\u0628',
                '\u062a',
                '\u062b',
                '\u062c',
                '\u062d',
                '\u062e',
                '\u062f',
                '\u0630',
                '\u0631',
                '\u0632',
                '\u0633',
                '\u0634',
                '\u0635',
                '\u0636',
                '\u0637',
                '\u0638',
                '\u0639',
                '\u063a',
                '\u0641',
                '\u0642',
                '\u0643',
                '\u0644',
                '\u0645',
                '\u0646',
                '\u0647',
                '\u0648',
                '\u064a',
                '\u0629'
            )
            val str24 = arrayOf(
                "\u0622",
                "\u064d\u0622",
                "\u0622\u064c",
                "\u0622\u064f",
                "\u0628\u0651",
                "\u062a\u064f",
                "\u062b\u064b",
                "\u062c\u064e",
                "\u062d",
                "\u062e\u064c",
                "\u062f\u064f",
                "\u0630\u064c",
                "\u0631",
                "\u0632",
                "\u0633\u064b\u0640",
                "\u0634\u0651\u0640",
                "\u0635\u0650\u0640",
                "\u0636\u0640",
                "\u0637\u064c\u0640",
                "\u0638\u064c\u0640",
                "\u0639\u064e",
                "\u063a\u0651",
                "\u0641\u064f",
                "\u0642\u064e",
                "\u06af",
                "\u0644\u064e",
                "\u0645\u0650\u0640",
                "\u0646",
                "\u0647\u0650\u06c1\u200f\u200f",
                "\u0648\u064f",
                "\u064a\u0640\ufbaf",
                "\u200f\u200f\u0647\u0650\u06c1\u064f\u200f\u200f\u064f\u064f"
            )
            var r23 = CharArray(31)
            r23 = charArrayOf(
                '\u0627',
                '\u0623',
                '\u0625',
                '\u0622',
                '\u0628',
                '\u062a',
                '\u062b',
                '\u062c',
                '\u062d',
                '\u062e',
                '\u062f',
                '\u0630',
                '\u0631',
                '\u0632',
                '\u0633',
                '\u0634',
                '\u0635',
                '\u0636',
                '\u0637',
                '\u0638',
                '\u0639',
                '\u063a',
                '\u0641',
                '\u0642',
                '\u0643',
                '\u0644',
                '\u0645',
                '\u0646',
                '\u0647',
                '\u0648',
                '\u064a'
            )
            val str25 = arrayOf(
                "\ufb51",
                "\ufb51",
                "\ufb51",
                "\ufb51",
                "\u0628",
                "\u067c",
                "\u062b",
                "\ufb75",
                "\u062d",
                "\u062e",
                "\u068a",
                "\u068e",
                "\u0631",
                "\u0697",
                "\u0633",
                "\u069c",
                "\u0635",
                "\u0636",
                "\u0637",
                "\u069f",
                "\u0639",
                "\u06a0",
                "\u0641",
                "\u06a6",
                "\u06af",
                "\u0644",
                "\u0645",
                "\u06b9",
                "\u06ff",
                "\u0648",
                "\u064a"
            )
            var r24 = CharArray(31)
            r24 = charArrayOf(
                '\u0627',
                '\u0623',
                '\u0625',
                '\u0622',
                '\u0628',
                '\u062a',
                '\u062b',
                '\u062c',
                '\u062d',
                '\u062e',
                '\u062f',
                '\u0630',
                '\u0631',
                '\u0632',
                '\u0633',
                '\u0634',
                '\u0635',
                '\u0636',
                '\u0637',
                '\u0638',
                '\u0639',
                '\u063a',
                '\u0641',
                '\u0642',
                '\u0643',
                '\u0644',
                '\u0645',
                '\u0646',
                '\u0647',
                '\u0648',
                '\u064a'
            )
            val str26 = arrayOf(
                "\u0622",
                "\u0622",
                "\u0622",
                "\u0622",
                "\u0628\ufba7",
                "\u062a\ufba7",
                "\u062b\ufba7",
                "\u062c\ufba7",
                "\u062d\ufba7",
                "\u062e\ufba7",
                "\u062f",
                "\u0630",
                "\u0631",
                "\u0632",
                "\u0633\ufba7",
                "\u0634\ufba7",
                "\u0635\ufba7",
                "\u0636\ufba7",
                "\u0637\ufba7",
                "\u0638\ufba7",
                "\u0639\ufba7",
                "\u063a\ufba7",
                "\u0641\ufba7",
                "\u0642\ufba7",
                "\u0643\ufba7",
                "\u0644\ufba7",
                "\u0645\ufba7",
                "\u0646\ufba7",
                "\u0647\ufba7",
                "\u0648",
                "\u064a\ufba7"
            )
            var r25 = CharArray(31)
            r25 = charArrayOf(
                '\u0627',
                '\u0623',
                '\u0625',
                '\u0622',
                '\u0628',
                '\u062a',
                '\u062b',
                '\u062c',
                '\u062d',
                '\u062e',
                '\u062f',
                '\u0630',
                '\u0631',
                '\u0632',
                '\u0633',
                '\u0634',
                '\u0635',
                '\u0636',
                '\u0637',
                '\u0638',
                '\u0639',
                '\u063a',
                '\u0641',
                '\u0642',
                '\u0643',
                '\u0644',
                '\u0645',
                '\u0646',
                '\u0647',
                '\u0648',
                '\u064a'
            )
            val str27 = arrayOf(
                "\u0622",
                "\u0622",
                "\u0622",
                "\u0622",
                "\u0628\u0640\u0640",
                "\u062a",
                "\u062b\u0640",
                "\u062c \u0640",
                "\u062d \u0640",
                "\u062e \u0640",
                "\u062f",
                "\u0630",
                "\u0631",
                "\u0632",
                "\u0633\u0640",
                "\u0634\u0640",
                "\u0635\u0640",
                "\u0636\u0640",
                "\u0637\u0640",
                "\u0638\u0640",
                "\u0639 \u0640",
                "\u063a \u0640",
                "\u0641",
                "\u0642",
                "\u0643",
                "\u0644",
                "\u0645\u0640",
                "\u0646",
                "\u0647",
                "\u0648",
                "\u064a"
            )
            var r26 = CharArray(31)
            r26 = charArrayOf(
                '\u0627',
                '\u0623',
                '\u0625',
                '\u0622',
                '\u0628',
                '\u062a',
                '\u062b',
                '\u062c',
                '\u062d',
                '\u062e',
                '\u062f',
                '\u0630',
                '\u0631',
                '\u0632',
                '\u0633',
                '\u0634',
                '\u0635',
                '\u0636',
                '\u0637',
                '\u0638',
                '\u0639',
                '\u063a',
                '\u0641',
                '\u0642',
                '\u0643',
                '\u0644',
                '\u0645',
                '\u0646',
                '\u0647',
                '\u0648',
                '\u064a'
            )
            val str28 = arrayOf(
                "\u0622",
                "\u0622",
                "\u0622",
                "\u0622",
                "\u0628\u06d2\u0640",
                "\u062a\u06d2\u0640",
                "\u062b\u06d2\u0640",
                "\u062c\u06d2\u0640",
                "\u062d\u06d2\u0640",
                "\u062e\u06d2\u0640",
                "\u062f",
                "\u0630",
                "\u0631",
                "\u0632",
                "\u0633\u06d2\u0640",
                "\u0634\u06d2\u0640",
                "\u0635\u06d2\u0640",
                "\u0636\u06d2\u0640",
                "\u0637\u06d2\u0640",
                "\u0638\u06d2\u0640",
                "\u0639\u06d2\u0640",
                "\u063a\u06d2\u0640",
                "\u0641\u06d2\u0640",
                "\u0642\u06d2\u0640",
                "\u0643\u06d2\u0640",
                "\u0644",
                "\u0645\u06d2\u0640",
                "\u0646\u06d2\u0640",
                "\u0647\u06d2\u0640\u0650",
                "\u0648",
                "\u064a\u06d2\u0640"
            )
            var r27 = CharArray(32)
            r27 = charArrayOf(
                '\u0627',
                '\u0623',
                '\u0625',
                '\u0622',
                '\u0628',
                '\u062a',
                '\u062b',
                '\u062c',
                '\u062d',
                '\u062e',
                '\u062f',
                '\u0630',
                '\u0631',
                '\u0632',
                '\u0633',
                '\u0634',
                '\u0635',
                '\u0636',
                '\u0637',
                '\u0638',
                '\u0639',
                '\u063a',
                '\u0641',
                '\u0642',
                '\u0643',
                '\u0644',
                '\u0645',
                '\u0646',
                '\u0647',
                '\u0648',
                '\u064a',
                '\u0629'
            )
            val str29 = arrayOf(
                "\u0622",
                "\u0622",
                "\u0622",
                "\u0622",
                "\u0628",
                "\u062a",
                "\u062b",
                "\u062c",
                "\u062d",
                "\u062e",
                "\u062f",
                "\u0630",
                "\u0631",
                "\u0632",
                "\u0633\u0640",
                "\u0634\u0640",
                "\u0635\u0640",
                "\u0636\u0640",
                "\u0637\u0640",
                "\u0638\u0640",
                "\u0639",
                "\u063a",
                "\u0641",
                "\u0642",
                "\u06af",
                "\u0644",
                "\u0645\u0640",
                "\u0646",
                "\u0647\u06c1\u200f\u200f",
                "\u0648",
                "\u064a\u0640\ufbaf",
                "\u200f\u200f\u0647\u06c1"
            )
            for (i in 0 until org.length) {
                j = 0
                while (j < r19.size) {
                    if (org[i] == r19[j]) {
                        org = org.replace(
                            StringBuilder(org[i].toString()).toString().toRegex(), StringBuilder(
                                str2[j]
                            ).toString()
                        )
                        r19[j] = '\u0000'
                        str2[j] = ""
                    }
                    j++
                }
                j = 0
                while (j < r20.size) {
                    if (o0[i] == r20[j]) {
                        o0 = o0.replace(
                            StringBuilder(o0[i].toString()).toString().toRegex(), StringBuilder(
                                str22[j]
                            ).toString()
                        )
                        r20[j] = '\u0000'
                        str22[j] = ""
                    }
                    j++
                }
            }
            for (k in 0 until o3.length) {
                j = 0
                while (j < r21.size) {
                    if (o3[k] == r21[j]) {
                        o3 = o3.replace(
                            StringBuilder(o3[k].toString()).toString().toRegex(), StringBuilder(
                                str23[j]
                            ).toString()
                        )
                        r21[j] = '\u0000'
                        str23[j] = ""
                    }
                    j++
                }
            }
            for (m in 0 until o4.length) {
                j = 0
                while (j < r22.size) {
                    if (o4[m] == r22[j]) {
                        o4 = o4.replace(
                            StringBuilder(o4[m].toString()).toString().toRegex(), StringBuilder(
                                str24[j]
                            ).toString()
                        )
                        r22[j] = '\u0000'
                        str24[j] = ""
                    }
                    j++
                }
            }
            for (s in 0 until o5.length) {
                j = 0
                while (j < r23.size) {
                    if (o5[s] == r23[j]) {
                        o5 = o5.replace(
                            StringBuilder(o5[s].toString()).toString().toRegex(), StringBuilder(
                                str25[j]
                            ).toString()
                        )
                        r23[j] = '\u0000'
                        str25[j] = ""
                    }
                    j++
                }
            }
            for (n in 0 until o6.length) {
                j = 0
                while (j < r24.size) {
                    if (o6[n] == r24[j]) {
                        o6 = o6.replace(
                            StringBuilder(o6[n].toString()).toString().toRegex(), StringBuilder(
                                str26[j]
                            ).toString()
                        )
                        r24[j] = '\u0000'
                        str26[j] = ""
                    }
                    j++
                }
            }
            for (b in 0 until o7.length) {
                j = 0
                while (j < r25.size) {
                    if (o7[b] == r25[j]) {
                        o7 = o7.replace(
                            StringBuilder(o7[b].toString()).toString().toRegex(), StringBuilder(
                                str27[j]
                            ).toString()
                        )
                        r25[j] = '\u0000'
                        str27[j] = ""
                    }
                    j++
                }
            }
            for (x in 0 until o8.length) {
                j = 0
                while (j < r26.size) {
                    if (o8[x] == r26[j]) {
                        o8 = o8.replace(
                            StringBuilder(o8[x].toString()).toString().toRegex(), StringBuilder(
                                str28[j]
                            ).toString()
                        )
                        r26[j] = '\u0000'
                        str28[j] = ""
                    }
                    j++
                }
            }
            for (u in 0 until o9.length) {
                j = 0
                while (j < r27.size) {
                    if (o9[u] == r27[j]) {
                        o9 = o9.replace(
                            StringBuilder(o9[u].toString()).toString().toRegex(), StringBuilder(
                                str29[j]
                            ).toString()
                        )
                        r27[j] = '\u0000'
                        str29[j] = ""
                    }
                    j++
                }
            }
            out1.setText(org)
            out2.setText(o0)
            out3.setText(o3)
            out4.setText(o4)
            out5.setText(o5)
            out6.setText(o6)
            out7.setText(o7)
            out8.setText(o8)
            out9.setText(o9)
        })


        copy1.setOnClickListener(View.OnClickListener {
            (requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE)
                    as ClipboardManager).text = out1.getText().toString()
            Toast.makeText(
                requireActivity().getBaseContext(),
                "\u062a\u0645 \u0646\u0633\u062e" +
                        " \u0645\u062d\u062a\u0648\u0649" +
                        " \u0645\u0631\u0628\u0639 \u0627\u0644\u0646\u0635 \u0627\u0644\u0646\u0627\u062a\u062c",
                Toast.LENGTH_SHORT
            ).show()
        })


        copy2.setOnClickListener(View.OnClickListener {
            (requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE)
                    as ClipboardManager).text = out1.getText().toString()
            Toast.makeText(
                requireActivity().getBaseContext(),
                "\u062a\u0645 \u0646\u0633\u062e" +
                        " \u0645\u062d\u062a\u0648\u0649" +
                        " \u0645\u0631\u0628\u0639 \u0627\u0644\u0646\u0635 \u0627\u0644\u0646\u0627\u062a\u062c",
                Toast.LENGTH_SHORT
            ).show()
        })


        copy3.setOnClickListener(View.OnClickListener {
            (requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE)
                    as ClipboardManager).text = out1.getText().toString()
            Toast.makeText(
                requireActivity().getBaseContext(),
                "\u062a\u0645 \u0646\u0633\u062e" +
                        " \u0645\u062d\u062a\u0648\u0649" +
                        " \u0645\u0631\u0628\u0639 \u0627\u0644\u0646\u0635 \u0627\u0644\u0646\u0627\u062a\u062c",
                Toast.LENGTH_SHORT
            ).show()
        })


        copy4.setOnClickListener(View.OnClickListener {
            (requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE)
                    as ClipboardManager).text = out1.getText().toString()
            Toast.makeText(
                requireActivity().getBaseContext(),
                "\u062a\u0645 \u0646\u0633\u062e" +
                        " \u0645\u062d\u062a\u0648\u0649" +
                        " \u0645\u0631\u0628\u0639 \u0627\u0644\u0646\u0635 \u0627\u0644\u0646\u0627\u062a\u062c",
                Toast.LENGTH_SHORT
            ).show()
        })


        copy5.setOnClickListener(View.OnClickListener {
            (requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE)
                    as ClipboardManager).text = out1.getText().toString()
            Toast.makeText(
                requireActivity().getBaseContext(),
                "\u062a\u0645 \u0646\u0633\u062e" +
                        " \u0645\u062d\u062a\u0648\u0649" +
                        " \u0645\u0631\u0628\u0639 \u0627\u0644\u0646\u0635 \u0627\u0644\u0646\u0627\u062a\u062c",
                Toast.LENGTH_SHORT
            ).show()
        })


        copy6.setOnClickListener(View.OnClickListener {
            (requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE)
                    as ClipboardManager).text = out1.getText().toString()
            Toast.makeText(
                requireActivity().getBaseContext(),
                "\u062a\u0645 \u0646\u0633\u062e" +
                        " \u0645\u062d\u062a\u0648\u0649" +
                        " \u0645\u0631\u0628\u0639 \u0627\u0644\u0646\u0635 \u0627\u0644\u0646\u0627\u062a\u062c",
                Toast.LENGTH_SHORT
            ).show()
        })


        copy7.setOnClickListener(View.OnClickListener {
            (requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE)
                    as ClipboardManager).text = out1.getText().toString()
            Toast.makeText(
                requireActivity().getBaseContext(),
                "\u062a\u0645 \u0646\u0633\u062e" +
                        " \u0645\u062d\u062a\u0648\u0649" +
                        " \u0645\u0631\u0628\u0639 \u0627\u0644\u0646\u0635 \u0627\u0644\u0646\u0627\u062a\u062c",
                Toast.LENGTH_SHORT
            ).show()
        })


        copy8.setOnClickListener(View.OnClickListener {
            (requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE)
                    as ClipboardManager).text = out1.getText().toString()
            Toast.makeText(
                requireActivity().getBaseContext(),
                "\u062a\u0645 \u0646\u0633\u062e" +
                        " \u0645\u062d\u062a\u0648\u0649" +
                        " \u0645\u0631\u0628\u0639 \u0627\u0644\u0646\u0635 \u0627\u0644\u0646\u0627\u062a\u062c",
                Toast.LENGTH_SHORT
            ).show()
        })


        copy9.setOnClickListener(View.OnClickListener {
            (requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE)
                    as ClipboardManager).text = out1.getText().toString()
            Toast.makeText(
                requireActivity().getBaseContext(),
                "\u062a\u0645 \u0646\u0633\u062e" +
                        " \u0645\u062d\u062a\u0648\u0649" +
                        " \u0645\u0631\u0628\u0639 \u0627\u0644\u0646\u0635 \u0627\u0644\u0646\u0627\u062a\u062c",
                Toast.LENGTH_SHORT
            ).show()
        })

        share1.setOnClickListener(View.OnClickListener {
            val sharingIntent = Intent("android.intent.action.SEND")
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(
                "android.intent.extra.TEXT",
                out9.getText().toString()
            )
            startActivity(
                Intent.createChooser(
                    sharingIntent,
                    "\u0634\u0627\u0631\u0643 \u0628\u0648\u0627\u0633\u0637\u0629 :"
                )
            )
        })


        share2.setOnClickListener(View.OnClickListener {
            val sharingIntent = Intent("android.intent.action.SEND")
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(
                "android.intent.extra.TEXT",
                out9.getText().toString()
            )
            startActivity(
                Intent.createChooser(
                    sharingIntent,
                    "\u0634\u0627\u0631\u0643 \u0628\u0648\u0627\u0633\u0637\u0629 :"
                )
            )
        })


        share3.setOnClickListener(View.OnClickListener {
            val sharingIntent = Intent("android.intent.action.SEND")
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(
                "android.intent.extra.TEXT",
                out9.getText().toString()
            )
            startActivity(
                Intent.createChooser(
                    sharingIntent,
                    "\u0634\u0627\u0631\u0643 \u0628\u0648\u0627\u0633\u0637\u0629 :"
                )
            )
        })


        share4.setOnClickListener(View.OnClickListener {
            val sharingIntent = Intent("android.intent.action.SEND")
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(
                "android.intent.extra.TEXT",
                out9.getText().toString()
            )
            startActivity(
                Intent.createChooser(
                    sharingIntent,
                    "\u0634\u0627\u0631\u0643 \u0628\u0648\u0627\u0633\u0637\u0629 :"
                )
            )
        })


        share5.setOnClickListener(View.OnClickListener {
            val sharingIntent = Intent("android.intent.action.SEND")
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(
                "android.intent.extra.TEXT",
                out9.getText().toString()
            )
            startActivity(
                Intent.createChooser(
                    sharingIntent,
                    "\u0634\u0627\u0631\u0643 \u0628\u0648\u0627\u0633\u0637\u0629 :"
                )
            )
        })


        share6.setOnClickListener(View.OnClickListener {
            val sharingIntent = Intent("android.intent.action.SEND")
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(
                "android.intent.extra.TEXT",
                out9.getText().toString()
            )
            startActivity(
                Intent.createChooser(
                    sharingIntent,
                    "\u0634\u0627\u0631\u0643 \u0628\u0648\u0627\u0633\u0637\u0629 :"
                )
            )
        })


        share7.setOnClickListener(View.OnClickListener {
            val sharingIntent = Intent("android.intent.action.SEND")
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(
                "android.intent.extra.TEXT",
                out9.getText().toString()
            )
            startActivity(
                Intent.createChooser(
                    sharingIntent,
                    "\u0634\u0627\u0631\u0643 \u0628\u0648\u0627\u0633\u0637\u0629 :"
                )
            )
        })


        share8.setOnClickListener(View.OnClickListener {
            val sharingIntent = Intent("android.intent.action.SEND")
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(
                "android.intent.extra.TEXT",
                out9.getText().toString()
            )
            startActivity(
                Intent.createChooser(
                    sharingIntent,
                    "\u0634\u0627\u0631\u0643 \u0628\u0648\u0627\u0633\u0637\u0629 :"
                )
            )
        })


        share9.setOnClickListener(View.OnClickListener {
            val sharingIntent = Intent("android.intent.action.SEND")
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(
                "android.intent.extra.TEXT",
                out9.getText().toString()
            )
            startActivity(
                Intent.createChooser(
                    sharingIntent,
                    "\u0634\u0627\u0631\u0643 \u0628\u0648\u0627\u0633\u0637\u0629 :"
                )
            )
        })





    }


}