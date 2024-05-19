package com.example.interstellar

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)

        val adultTickets = arguments?.getString("adultTickets")?.toIntOrNull() ?: 0
        val childTickets = arguments?.getString("childTickets")?.toIntOrNull() ?: 0
        val selectedDate = arguments?.getString("selectedDate")
        val selectedTime = arguments?.getString("selectedTime")
        val totalPrice = adultTickets * 45 + childTickets * 25
        val totalPriceText = getString(R.string.total_price) + "${totalPrice.toString()}"

        view.apply {
            findViewById<TextView>(R.id.infoFragment_MovieName).text = getString(R.string.app_name)
            findViewById<TextView>(R.id.infoFragment_AdultTickets).text = (adultTickets.toString() + (" ") + getString(R.string.adult_tickets))
            findViewById<TextView>(R.id.infoFragment_ChildTickets).text = (childTickets.toString() + (" ") +  getString(R.string.child_tickets))
            findViewById<TextView>(R.id.infoFragment_SelectedDate).text = selectedDate
            findViewById<TextView>(R.id.infoFragment_SelectedTime).text = selectedTime
            findViewById<TextView>(R.id.infoFragment_TotalPrice).text = totalPriceText

            findViewById<Button>(R.id.infoFragment_ExitBTN).apply {
                text = getString(R.string.exit)
                setOnClickListener { showExitConfirmationDialog() }
            }
        }

        return view
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.exit_confirmation))
        builder.setMessage(getString(R.string.exit_promp))
        builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            requireActivity().finish()
        }
        builder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}