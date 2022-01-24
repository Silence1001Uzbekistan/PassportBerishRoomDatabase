package uz.artikov.passportberishroomdatabase

import Adapters.RvAdapter
import Adapters.RvOnClick
import Database.AppDatabase
import Models.Citizens
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import uz.artikov.passportberishroomdatabase.databinding.FragmentListBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {
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

    lateinit var binding: FragmentListBinding
    lateinit var rvAdapter: RvAdapter
    lateinit var appDatabase: AppDatabase
    lateinit var listData: ArrayList<Citizens>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentListBinding.inflate(LayoutInflater.from(context))

        binding.imageBackList.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imageListSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                val listSearch = ArrayList<Citizens>()

                for (citizens in listData) {
                    if (citizens.name?.subSequence(0, query?.length!!) == query) {
                        listSearch.add(citizens)
                    }
                }

                search(listSearch)
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val listSearch = ArrayList<Citizens>()

                Toast.makeText(requireContext(), "$newText", Toast.LENGTH_SHORT).show()
                if (newText!!.length == 3){
                    binding.imageListSearch.onActionViewCollapsed();
                    binding.imageListSearch.setQuery("", false);
                    binding.imageListSearch.clearFocus();
                    
                    binding.imageBackList.visibility = View.VISIBLE
                    binding.txtTitle.visibility = View.VISIBLE

                    search(listData)
                    return false


                }

                for (citizens in 0 until listData.size) {



                    if (listData[citizens].name!!.subSequence(0, 1).toString()
                            .equals(newText.toString(), ignoreCase = true)
                    ) {

                        listSearch.add(listData[citizens])
                    }

                }

                search(listSearch)
                return true

            }

        })

        binding.imageListSearch.setOnCloseListener(object : SearchView.OnCloseListener,
            androidx.appcompat.widget.SearchView.OnCloseListener {
            override fun onClose(): Boolean {

                binding.imageBackList.visibility = View.VISIBLE
                binding.txtTitle.visibility = View.VISIBLE

                search(listData)
                return false

            }

        })

        binding.imageListSearch.setOnSearchClickListener {

            binding.imageListSearch.clearFocus();
            binding.imageBackList.visibility = View.GONE
            binding.txtTitle.visibility = View.GONE

        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        appDatabase = AppDatabase.getInstance(binding.root.context)
        listData = ArrayList()
        listData.addAll(appDatabase.citizenDao().getAllCitizens())

        rvAdapter = RvAdapter(listData, object : RvOnClick {
            override fun itemOnClick(citizens: Citizens, position: Int) {
                findNavController().navigate(
                    R.id.aboutShowFragment,
                    bundleOf("keyCitizen" to citizens)
                )
            }

            override fun moreOnClick(citizens: Citizens, position: Int, v: ImageView) {

                val popupMenu = PopupMenu(context, v)
                popupMenu.inflate(R.menu.popup_menu)
                popupMenu.setOnMenuItemClickListener {

                    when (it.itemId) {

                        R.id.delete_menu -> {

                            val dialog = AlertDialog.Builder(context)
                            dialog.setMessage("${citizens.name} ${citizens.passportSeriya} fuqaro o'chirilsinmi?")
                            dialog.setNegativeButton("Ha") { dialog, which ->

                                var id = appDatabase.citizenDao()
                                    .getCitizenById(citizens.passportSeriya!!)
                                citizens.id = id
                                appDatabase.citizenDao().deleteCitizen(citizens)
                                Toast.makeText(
                                    context,
                                    "${citizens.id} deleted",
                                    Toast.LENGTH_SHORT
                                ).show()

                                onResume()

                            }
                            dialog.setPositiveButton("Yo'q") { dialog, which ->
                            }
                            dialog.show()
                        }

                        R.id.edit_menu -> {

                            findNavController().navigate(
                                R.id.editFragment,
                                bundleOf("citizensKey" to citizens)
                            )

                        }

                    }

                    true

                }

                popupMenu.show()

            }

        })

        binding.rvList.adapter = rvAdapter

    }


    fun search(list: List<Citizens>) {
        rvAdapter = RvAdapter(list, object : RvOnClick {
            override fun itemOnClick(citizens: Citizens, position: Int) {

                findNavController().navigate(
                    R.id.aboutShowFragment,
                    bundleOf("keyCitizen" to citizens)
                )

            }

            override fun moreOnClick(citizens: Citizens, position: Int, v: ImageView) {

                val popupMenu = PopupMenu(context, v)
                popupMenu.inflate(R.menu.popup_menu)
                popupMenu.setOnMenuItemClickListener {

                    when (it.itemId) {

                        R.id.delete_menu -> {

                            val dialog = AlertDialog.Builder(context)
                            dialog.setMessage("${citizens.name} ${citizens.passportSeriya} fuqaro o'chirilsinmi?")
                            dialog.setNegativeButton(
                                "Ha"
                            ) { dialog, which ->

                                val id = appDatabase.citizenDao()
                                    .getCitizenById(citizens.passportSeriya!!)
                                citizens.id = id
                                appDatabase.citizenDao().deleteCitizen(citizens)
                                Toast.makeText(
                                    context,
                                    "${citizens.id} deleted",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onResume()

                            }
                            dialog.setPositiveButton("Yo'q") { dialog, which ->
                            }
                            dialog.show()
                        }

                        R.id.edit_menu -> {
                            findNavController().navigate(

                                R.id.editFragment,
                                bundleOf("citizensKey" to citizens)

                            )
                        }

                    }

                    true

                }
                popupMenu.show()
            }

        })

        binding.rvList.adapter = rvAdapter

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}