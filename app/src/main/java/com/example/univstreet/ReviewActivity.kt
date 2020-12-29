package com.example.univstreet

import android.Manifest
import android.app.Activity
import android.content.CursorLoader
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_review.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ReviewActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    val PICK_IMAGE_REQUEST = 1
    var filePath : String? = null
    var fileName: String? = null
    var upLoadServerUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        upLoadServerUri = "http://" + "192.168.0.178" + "/UploadToServer.php"//서버컴퓨터의 ip주소

        imageView.setImageResource(R.drawable.picture)
        ActivityCompat.requestPermissions(
            this@ReviewActivity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )

        upload_btn.setOnClickListener{

            var thread = Thread(Runnable {
                if (filePath != null)
                //uploadFile(filePath)
                    upload(filePath)
            })

            thread.start()

        }

        try{

            val retClient = Okhttp3RetrofitManager.getRetrofitService(Service2::class.java)
            val retrofit = retClient.ReviewImgApi("5_Nightscape.jpg")
            retrofit.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    t.printStackTrace()
                }
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.body()!=null){

                        var inputStream  = response.body()!!.byteStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)

                        imageView.setImageBitmap(bitmap)

                        //Toast.makeText(applicationContext, "다운로드 성공", Toast.LENGTH_LONG).show()

                    }
                }
            })

        } catch (e : java.lang.Exception){

            Toast.makeText(applicationContext, "다운로드 실패!", Toast.LENGTH_LONG).show()
        }



        imageView.setOnClickListener{

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)

        }

    }

    fun upload(sourceFileUri : String?){

        try{

            val sourceFile = File(sourceFileUri)

            val retClient = Okhttp3RetrofitManager.getRetrofitService(Service2::class.java)

            var requestBody : RequestBody = RequestBody.create(MediaType.parse("image/*"),sourceFile)
            var body : MultipartBody.Part = MultipartBody.Part.createFormData("uploaded_file",fileName,requestBody)


            val retrofit = retClient.post_Porfile_Request(body)
            retrofit.enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    t.printStackTrace()
                }
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(response.body() != null){

                        Toast.makeText(applicationContext, "이미지 업로드 성공", Toast.LENGTH_LONG).show()

                    }
                }
            })

        } catch (e : java.lang.Exception){

            Toast.makeText(applicationContext, "삽입 실패!", Toast.LENGTH_LONG).show()
        }

    }

    private fun getRealPathFromURI(contentUri: Uri?):String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)

        val cursorLoader = CursorLoader(this, contentUri, proj, null, null, null)
        val cursor = cursorLoader.loadInBackground()

        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

        cursor.moveToFirst()

        return cursor.getString(column_index)
    }

    private fun getImgName(contentUri: Uri?):String? {

        val returnCursor = contentResolver.query(contentUri!!, null, null, null, null)
        try {
            val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor!!.moveToFirst()
            fileName = returnCursor!!.getString(nameIndex)

        } catch (e:Exception) {

        } finally {
            returnCursor!!.close()
        }

        return fileName

    }

    //이미지 선택작업을 후의 결과 처리
    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            //이미지를 하나 골랐을때
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && null != data) {
                //data에서 절대경로로 이미지를 가져옴
                val uri = data!!.data
                filePath = (getRealPathFromURI(uri)).toString()

                fileName = getImgName(uri)

                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                //이미지가 한계이상(?) 크면 불러 오지 못하므로 사이즈를 줄여 준다.
                val nh = (bitmap.height * (1024.0 / bitmap.width)).toInt()
                val scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true)


                imageView.setImageBitmap(scaled)
            } else {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show()
            }

        } catch (e:Exception) {
            Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }

}
