package com.aboubakergb.mymemory

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.aboubakergb.mymemory.models.BoardSize
import com.aboubakergb.mymemory.utils.EXTRA_BOARD_SIZE
import com.aboubakergb.mymemory.utils.isPermissionGranted
import com.aboubakergb.mymemory.utils.requestPermission
import kotlinx.android.synthetic.main.activity_create_activiy.*

class CreateActiviy : AppCompatActivity() {

    private lateinit var adapter: ImagePickerAdapter
    private lateinit var boardSize: BoardSize
    private var numImageRequired=-1
    private var chosenImageUris= mutableListOf<Uri>()

    companion object{
        private const val TAG ="CreateActiviy"
        private const val PICK_PHOTO_CODE=655
        private const val READ_EXTERNAL_PHOTOS_CODE=248
        private const val READ_PHOTOS_PERMISSION=android.Manifest.permission.READ_EXTERNAL_STORAGE

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_activiy)
         // et back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        boardSize = intent.getSerializableExtra(EXTRA_BOARD_SIZE) as BoardSize
        numImageRequired=boardSize.getPairs()
        supportActionBar?.title="Choose pics ( 0/ $numImageRequired)"

        adapter=ImagePickerAdapter(this,chosenImageUris,boardSize, object: ImagePickerAdapter.ImageClickListener{

            override fun onPlaceHolderClicked() {
                //check user if the user has granted permission or not
                if (isPermissionGranted(this@CreateActiviy, READ_PHOTOS_PERMISSION)){
                    launchIntentForPhotos()
                    // if permission not granted
                }else{
                    requestPermission(this@CreateActiviy, READ_PHOTOS_PERMISSION, READ_EXTERNAL_PHOTOS_CODE)
                }
            }

        })
        rvImagePicker.adapter=adapter
        rvImagePicker.layoutManager=GridLayoutManager(this,boardSize.getWidth())

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // check if permission  granted
        if (requestCode== READ_EXTERNAL_PHOTOS_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                launchIntentForPhotos()
            }else{
                Toast.makeText(this,"In Order to create a custom game, you need to provide access to your photos",Toast.LENGTH_LONG).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId== android.R.id.home){
            finish()
        return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode!= PICK_PHOTO_CODE || resultCode != Activity.RESULT_OK || data == null ){
            Log.w(TAG ,"Did not get back from the launched activity, user likely canceled flow")
            return
        }
        val selectedUri =data.data
        val clipData=data.clipData
        // check clip if date is not null
        if (clipData != null ){
            Log.i(TAG," clipData numImages ${clipData.itemCount} : $clipData ")
            for (i in 0 until clipData.itemCount){
                val clipItem=clipData.getItemAt(i)
                if(chosenImageUris.size < numImageRequired){
                    chosenImageUris.add(clipItem.uri)
                }
            }
            }else if(selectedUri != null){
            Log.i(TAG , "data: $selectedUri")
            chosenImageUris.add(selectedUri)
        }
        adapter.notifyDataSetChanged()
        supportActionBar?.title="Choose pics (${chosenImageUris.size} / ${numImageRequired} )"
        btnSave.isEnabled=shouldEnableSaveButton()


    }

    private fun shouldEnableSaveButton(): Boolean {
        // check if we should enable save button or not 
        return true
    }

    // select photos from phone
    private fun launchIntentForPhotos() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        startActivityForResult(Intent.createChooser(intent,"choose pics"),PICK_PHOTO_CODE)

    }
}

