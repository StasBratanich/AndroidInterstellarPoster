// MovieFragment.kt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.example.interstellar.R
import com.example.interstellar.ViewPagerAdapter
import me.relex.circleindicator.CircleIndicator3

class MovieFragment : Fragment() {

    private var imagesList = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie, container, false)

        val viewPager2: ViewPager2 = view.findViewById(R.id.view_pager2)
        val getTicketsBTN: Button = view.findViewById(R.id.Button_GetTickets)
        val titleTextView: TextView = view.findViewById(R.id.TextView_Interstellar)
        val descriptionTextView: TextView = view.findViewById(R.id.TextView_Description)
        val directorTextView: TextView = view.findViewById(R.id.FragmentMovie_Director)
        val starsTextView: TextView = view.findViewById(R.id.FragmentMovie_Stars)

        titleTextView.text = getString(R.string.app_name)
        descriptionTextView.text = getString(R.string.movie_description)
        directorTextView.setText(R.string.director)
        starsTextView.setText(R.string.stars)

        loadImages()
        viewPager2.adapter = ViewPagerAdapter(imagesList)

        val indicator: CircleIndicator3 = view.findViewById(R.id.CircleIndicator)
        indicator.setViewPager(viewPager2)

        getTicketsBTN.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_movieFragment_to_purchaseFragment)
        }

        return view
    }

    private fun loadImages() {
        val resources = requireContext().resources
        val packageName = requireContext().packageName

        for (i in 1..5) {
            val resName = "image$i"
            val resId = resources.getIdentifier(resName, "drawable", packageName)
            if (resId != 0) {
                imagesList.add(resId)
            }
        }
    }
}
