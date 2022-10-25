package com.maylinda.mynote.fragments
//TODO 11: Apabila hendak melakukan perubahan pada note yang telah dibuat maka activity fragment UpdateNoteFragment berjalan
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.maylinda.mynote.MainActivity
import com.maylinda.mynote.R
import com.maylinda.mynote.databinding.FragmentUpdateNoteBinding
import com.maylinda.mynote.model.Note
import com.maylinda.mynote.toast
import com.maylinda.mynote.viewmodel.NoteViewModel

//Kelas ini mengakses fragment_update_note sebagai layout
class UpdateNoteFragment : Fragment(R.layout.fragment_update_note) {

    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!

    private val args: UpdateNoteFragmentArgs by navArgs()
    private lateinit var currentNote: Note
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateNoteBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

   //Membuat fungsi onViewCreated
    //mengakses fungsi updateNote yang ada pada noteViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = (activity as MainActivity).noteViewModel
        currentNote = args.note!!

       //Merubah data etNoteBody dan etNoteTitle dari yang semula telah menjadi string menjadi teks kembali
        binding.etNoteBodyUpdate.setText(currentNote.noteBody)
        binding.etNoteTitleUpdate.setText(currentNote.noteTitle)

       //Saat user telah melakukan update maka data etNoteTitle dan etNoteBody kembali diubah menjadi string
        binding.fabDone.setOnClickListener {
            val title = binding.etNoteTitleUpdate.text.toString().trim()
            val body = binding.etNoteBodyUpdate.text.toString().trim()


//Menggunakan perulangan if yang jika user telah memperbaharui bagian  noteTitle maka  variabel note dimana Note berisi id,noteTitle,dan NoteBody
            if (title.isNotEmpty()) {
                val note = Note(currentNote.id, title, body)
                noteViewModel.updateNote(note)
                //Memanggil fungsi updateNote yang ada pada noteViewModel untuk diterapkan pada note.
                //Menampilkan hasil note dari newnotefragment kembali ke homefragment
                view.findNavController().navigate(R.id.action_updateNoteFragment_to_homeFragment)
                // menggunakan else untuk kondisi dimana noteTitle masih kosong atau tidak terisi
                //Menampilkan umpan balik "Tulis Judul Dulu"
            } else {
                activity?.toast("Tulis Judul Dulu")
            }
        }
    }
//Membuat fungsi deleteNote
    //Saat user menekan icon trash cans maka muncul alert dialog dengan judul Hapus Note dan pesan Yakin Mau Hapus Note?,
// Maka terdapat dua buah button yakni HAPUS dan BATAL jika user menekan button HAPUS maka akan memanggil fungsi deleteNote yang ada pada NoteViewModel
    private fun deleteNote() {
        AlertDialog.Builder(activity).apply {
            setTitle("Hapus Note")
            setMessage("Yakin Mau Hapus Note?")
            setPositiveButton("HAPUS") { _, _ ->
                noteViewModel.deleteNote(currentNote)
                view?.findNavController()?.navigate(
                    R.id.action_updateNoteFragment_to_homeFragment
                )
            }
            setNegativeButton("BATAL", null)
        }.create().show()

    }

    //Fungsi dibawah ini mengarahkan ke file menu_update_note.xml
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_update_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    //Fungsi dibawah ini dijalankan jika ote yang dipilih akan  dihapus maka note dengan id tersebut akan dihapus.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                deleteNote()
            }
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}