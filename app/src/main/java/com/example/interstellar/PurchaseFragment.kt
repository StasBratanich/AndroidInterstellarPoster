package com.example.interstellar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import java.util.Calendar

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PurchaseFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_purchase, container, false)

        view.apply {
            findViewById<TextView>(R.id.purchaseFragment_Parentsguide).text = getString(R.string.parents_guide)
            findViewById<TextView>(R.id.purchaseFragment_MovieLength).text = getString(R.string.movie_length)
            findViewById<TextView>(R.id.purchaseFragment_ImdbRatinmg).text = getString(R.string.imdb_score)
            findViewById<TextView>(R.id.purchaseFragment_Adult45NIS).text = getString(R.string.adult45nis)
            findViewById<TextView>(R.id.purchaseFragment_Child25NIS).text = getString(R.string.child25nis)
            findViewById<TextView>(R.id.purchaseFragment_TextChooseCinema).text = getString(R.string.choose_cinema)
            findViewById<TextView>(R.id.purchaseFragment_TextChooseDate).text = getString(R.string.choose_date)
            findViewById<TextView>(R.id.purchaseFragment_TextChooseTime).text = getString(R.string.choose_time)
        }

        setupSpinners(view)
        setupDateAndTimePickers(view)

        view.findViewById<Button>(R.id.purchaseFragment_BuyBTN).apply {
            text = getString(R.string.buy_tickets)
            setOnClickListener { onBuyClicked() }
        }

        return view
    }

    private fun setupSpinners(view: View) {
        val ticketOptions = arrayOf("0", "1", "2", "3", "4", "5")
        val ticketsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ticketOptions)
        ticketsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        view.apply {
            findViewById<Spinner>(R.id.purchaseFragment_AdultTicketPicker).apply {
                adapter = ticketsAdapter
                prompt = getString(R.string.adult_tickets)
            }
            findViewById<Spinner>(R.id.purchaseFragment_ChildTicketPicker).apply {
                adapter = ticketsAdapter
                prompt = getString(R.string.child_tickets)
            }
            findViewById<Spinner>(R.id.purchaseFragment_CinemaPicker).apply {
                adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    resources.getStringArray(R.array.cinema_options)
                )
                prompt = getString(R.string.choose_cinema)
            }
        }
    }

    private fun setupDateAndTimePickers(view: View) {
        val currentDate = Calendar.getInstance()
        view.findViewById<Button>(R.id.purchaseFragment_DateBTN).apply {
            text = "${formatDigit(currentDate.get(Calendar.DAY_OF_MONTH))}/${formatDigit(currentDate.get(Calendar.MONTH) + 1)}/${currentDate.get(Calendar.YEAR)}"
            setOnClickListener { showDatePickerDialog() }
        }

        view.findViewById<Button>(R.id.purchaseFragment_TimeBTN).apply {
            text = getCurrentTime()
            setOnClickListener { showTimePickerDialog() }
        }
    }

    private fun getCurrentTime(): String {
        val currentTime = Calendar.getInstance()
        return "${formatDigit(currentTime.get(Calendar.HOUR_OF_DAY))}:${formatDigit(currentTime.get(Calendar.MINUTE))}"
    }

    private fun showTimePickerDialog() {
        val currentTime = Calendar.getInstance()
        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentTime.get(Calendar.MINUTE)

        TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val selectedTime = "${formatDigit(selectedHour)}:${formatDigit(selectedMinute)}"
                val selectedDateParts = view?.findViewById<Button>(R.id.purchaseFragment_DateBTN)?.text?.split("/")
                val selectedDay = selectedDateParts?.get(0)?.toIntOrNull() ?: 0
                val selectedMonth = selectedDateParts?.get(1)?.toIntOrNull()?.minus(1) ?: 0
                val selectedYear = selectedDateParts?.get(2)?.toIntOrNull() ?: 0
                val selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0)
                }
                if (selectedDate.timeInMillis > currentTime.timeInMillis) {
                    view?.findViewById<Button>(R.id.purchaseFragment_TimeBTN)?.text = selectedTime
                } else {
                    Toast.makeText(requireContext(), getString(R.string.time_prompt), Toast.LENGTH_SHORT).show()
                }
            },
            currentHour,
            currentMinute,
            true
        ).show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = "${formatDigit(selectedDayOfMonth)}/${formatDigit(selectedMonth + 1)}/$selectedYear"
                view?.findViewById<Button>(R.id.purchaseFragment_DateBTN)?.text = selectedDate
            },
            year,
            month,
            dayOfMonth
        ).apply {
            datePicker.minDate = System.currentTimeMillis() - 1000
        }.show()
    }

    private fun onBuyClicked() {
        val adultTickets = view?.findViewById<Spinner>(R.id.purchaseFragment_AdultTicketPicker)?.selectedItem.toString().toInt()
        val childTickets = view?.findViewById<Spinner>(R.id.purchaseFragment_ChildTicketPicker)?.selectedItem.toString().toInt()

        if (adultTickets > 0 || childTickets > 0) {
            val bundle = Bundle().apply {
                putString("adultTickets", adultTickets.toString())
                putString("childTickets", childTickets.toString())
                putString("selectedDate", view?.findViewById<Button>(R.id.purchaseFragment_DateBTN)?.text.toString())
                putString("selectedTime", view?.findViewById<Button>(R.id.purchaseFragment_TimeBTN)?.text.toString())
            }
            Navigation.findNavController(requireView()).navigate(R.id.action_purchaseFragment_to_infoFragment, bundle)
        } else {
            Toast.makeText(requireContext(), getString(R.string.purchase_prompt), Toast.LENGTH_SHORT).show()
        }
    }

    private fun formatDigit(digit: Int): String = if (digit < 10) "0$digit" else digit.toString()

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PurchaseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
