package com.maylinda.mynote
//TODO 4 : Pada AndroidManifest, MainActivity menjadi target activity pertama dalam pembuatan aplikasi mynote ini.
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.maylinda.mynote.databinding.ActivityMainBinding
import com.maylinda.mynote.db.NoteDatabase
import com.maylinda.mynote.repository.NoteRepository
import com.maylinda.mynote.viewmodel.NoteViewModel
import com.maylinda.mynote.viewmodel.NoteViewModelProviderFactory

class MainActivity : AppCompatActivity() {
    //Menggunakan viewbinding untuk mengakses layout activity_main.
    //pendeklarasian variabel noteviewmodel dengan target activity NoteViewModel.
    private lateinit var binding: ActivityMainBinding
    lateinit var noteViewModel: NoteViewModel

    //menjadikan activity sebagai kelas binding untuk mengakses variabel dan tampilan tata letak.
    //menggunakan metode inflate dalam membuat instance dari class binding activity main
    //mensetting activity main sebagai action bar.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setUpViewModel()
    }

    //Membuat fungsi untuk menyiapkan model tampilan.
    //Menerima informasi perubahan data.
    private fun setUpViewModel() {
        val noteRepository = NoteRepository(
            NoteDatabase(this)
        )

        val viewModelProviderFactory =
            NoteViewModelProviderFactory(
                application, noteRepository
            )

        noteViewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        ).get(NoteViewModel::class.java)
    }

}