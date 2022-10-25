package com.maylinda.mynote.fragments
//TODO 8 : Saat button fabAddNote ditekan maka fragment beralih dari homefragment ke newnotefragment
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.maylinda.mynote.MainActivity
import com.maylinda.mynote.R
import com.maylinda.mynote.databinding.FragmentNewNoteBinding
import com.maylinda.mynote.model.Note
import com.maylinda.mynote.toast
import com.maylinda.mynote.viewmodel.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*

//kelas mengakses fragment_new_note sebagai layoutnya.
class NewNoteFragment : Fragment(R.layout.fragment_new_note) {
    //deklarasi variabel binding
    //deklarasi variabel noteViewModel yang memuat NoteViewModel
    //deklarasi variabel mView
    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var mView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewNoteBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

   //mereferensikan fungsi di class induk dengan menggunakan kata kunci super
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = (activity as MainActivity).noteViewModel
        mView = view
    }

    //mendeklarasikan fungsi save saat user menekan button save.
    //membuat variabel noteTitle dengan binding mengakses id etNoteTitle yang merubah dari data teks menjadi string.
    //membuat variabel noteBody dengan binding mengkases id etNoteBody yang merubah dari data teks menjadi string.
    private fun saveNote(view: View) {
        val noteTitle = binding.etNoteTitle.text.toString().trim()
        val noteBody = binding.etNoteBody.text.toString().trim()

       //Menggunakan perulangan if yang jika user telah mengisi bagian  noteTitle maka  variabel note dimana Note berisi id,noteTitle,dan NoteBody
        if (noteTitle.isNotEmpty()) {
            val note = Note(0, noteTitle, noteBody)
           //Memanggil fungsi addNote yang ada pada noteViewModel untuk diterapkan pada note.
            //Memberikan umpan balik "NoteBerhasil di Simpan" dengan menggunakan snackbar ketika berhasil menjalankan addNote.
            //Menampilkan hasil note dari newnotefragment kembali ke homefragment
            noteViewModel.addNote(note)
            Snackbar.make(
                view, "Note Berhasil di Simpan",
                Snackbar.LENGTH_SHORT
            ).show()
            view.findNavController().navigate(R.id.action_newNoteFragment_to_homeFragment)
         // menggunakan else untuk kondisi dimana noteTitle masih kosong atau tidak terisi
            //Menampilkan umpan balik "Tulis Judul Dulu"
        } else {
            activity?.toast("Tulis Judul Dulu")
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_new_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                saveNote(mView)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}