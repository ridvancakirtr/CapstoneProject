package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_specialty_add.*

class SpecialtyAddActivity : AppCompatActivity() {
    var ref= FirebaseDatabase.getInstance().reference
    var specialtyBranches=SpecialtyBranches()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specialty_add)
        val actionBar=supportActionBar
        actionBar!!.title="Specialty Branches Add"
        actionBar.setDisplayHomeAsUpEnabled(true)

        btnSpecialty.setOnClickListener {
            addSpecialty()
        }
    }

    private fun addSpecialty() {
        if(!specialtyText.text.isEmpty()){
            var id=ref.push().key
            specialtyBranches.SpecialtyBranches=specialtyText.text.toString()
            specialtyBranches.SpecialtyBranches_id=id
            ref.child("SpecialtyBranches")
                    .child(id)
                    .setValue(specialtyBranches)
                    .addOnCompleteListener {task->
                        if (task.isSuccessful){
                            Toast.makeText(this@SpecialtyAddActivity,"Specialty is Saved", Toast.LENGTH_SHORT).show()
                            redirectMainPage()
                        }
                        else{
                            Toast.makeText(this@SpecialtyAddActivity,"ERROR Specialty is Not Saved", Toast.LENGTH_SHORT).show()
                        }
                    }
        }else{
            Toast.makeText(this@SpecialtyAddActivity,"Please Enter Blank Field", Toast.LENGTH_SHORT).show()
        }


    }

    private fun redirectMainPage(){
        var redirectMainPage= Intent(this@SpecialtyAddActivity, SpecialtyManagementActivity::class.java)
        startActivity(redirectMainPage)
    }

    override fun onSupportNavigateUp(): Boolean {
        var intent = Intent(this@SpecialtyAddActivity,SpecialtyManagementActivity::class.java)
        startActivity(intent)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.exitToolbar -> {
            var intent = Intent(this@SpecialtyAddActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@SpecialtyAddActivity, "Log Out", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)

        }
    }
}
