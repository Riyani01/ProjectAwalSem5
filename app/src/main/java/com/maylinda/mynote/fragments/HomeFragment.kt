  package com.maylinda.mynote.fragments
//TODO 6: Activity fragment pertama yang dijalankan adalah HomeFragment


import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.maylinda.mynote.MainActivity
import com.maylinda.mynote.R
import com.maylinda.mynote.adapter.NoteAdapter
import com.maylinda.mynote.databinding.FragmentHomeBinding
import com.maylinda.mynote.model.Note
import com.maylinda.mynote.viewmodel.NoteViewModel

//Activity ini mengakses layout fragment home
class HomeFragment : Fragment(R.layout.fragment_home),
    SearchView.OnQueryTextListener {
    //deklarasi variabel binding
    //deklarasi variabel noteViewModel yang memuat NoteViewModel
    //deklarasi variabel noteAdapter yang memuat NoteAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var notesViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter


    //Membuat fungsi untuk mensetting opsi menu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesViewModel = (activity as MainActivity).noteViewModel
        setUpRecyclerView()
        //mengarahkan target ketika button fabAddNote ditekan maka akan berganti dari homefragment ke newnotefragment.
        binding.fabAddNote.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_newNoteFragment)
        }
    }


    //TODO 10 : Saat note berhasil dibuat maka halaman diarahkan ke home
    //Membuat fungsi untuk mensetting recyclerview saat telah membuat note
    //Menggunakan NoteAdapter dalam menampilkan recyclerview
    private fun setUpRecyclerView() {
        noteAdapter = NoteAdapter()

        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
            adapter = noteAdapter
        }
        //aktifitas yang berjalan yakni mengizinkan noteViewModel mengambil semua note untuk diobservasi sehingga noteAdapter menyimpan list note yang berbeda dan mengupdate tampilan.
        activity?.let {
            notesViewModel.getAllNote().observe(viewLifecycleOwner, { note ->
                noteAdapter.differ.submitList(note)
                updateUI(note)
            })
        }

    }


    //Membuat fungsi untuk memperbaharui tampilan ketika telah membuat note
    private fun updateUI(note: List<Note>) {
        if (note.isNotEmpty()) {
            binding.cardView.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        } else {
            binding.cardView.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        }
    }

    //membuat fungsi untuk mengatur menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.home_menu, menu)
        val mMenuSearch = menu.findItem(R.id.menu_search).actionView as SearchView
        mMenuSearch.isSubmitButtonEnabled = false
        mMenuSearch.setOnQueryTextListener(this)

    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        /*if (query != null) {
            searchNote(query)
        }*/
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        if (newText != null) {
            searchNote(newText)
        }
        return true
    }

   //membuat fungsi untuk mencari note
    private fun searchNote(query: String?) {
        val searchQuery = "%$query%"
        notesViewModel.searchNote(searchQuery).observe(
            this, { list ->
                noteAdapter.differ.submitList(list)
            }
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}