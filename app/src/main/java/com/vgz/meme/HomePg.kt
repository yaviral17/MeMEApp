package com.vgz.meme

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_home_pg.*

class HomePg : AppCompatActivity() {

    val URL = "https://meme-api.herokuapp.com/gimme"
    var Purl = ""

    lateinit var Curl: String
    var current_url = 1
    var press_count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_pg)

//        MobileAds.initialize(this)


        Curl_textview.visibility = View.INVISIBLE



        loadmeme()
        next_btn.setOnClickListener {
            if(memeprogressbar.visibility == View.VISIBLE){
                Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
            }else {
                if (press_count == 1) {
                    current_url = 1
                    memeprogressbar.visibility = View.VISIBLE
                    Curl_textview.text = Curl
                    Glide.with(this).load(Curl).listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            memeprogressbar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            memeprogressbar.visibility = View.GONE
                            return false

                        }

                    }).into(meme_img)
                    press_count = 0
                } else {
                    loadmeme()
                    current_url = 1
                    Purl = Curl
                }
            }
        }

        previous_btn.setOnClickListener {
            if (press_count < 1 && Purl.isNotEmpty()) {
                memeprogressbar.visibility = View.VISIBLE
                Curl_textview.setText(Purl)
                Glide.with(this).load(Purl).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        memeprogressbar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        memeprogressbar.visibility = View.GONE
                        return false

                    }

                }).into(meme_img)
                current_url = 0
                press_count += 1
            }
            else{
                if (Purl.isNotEmpty()) {
                    Toast.makeText(this, "Only one previous meme can be seen!", Toast.LENGTH_SHORT)
                        .show()
                }
                else{
                    Toast.makeText(this, "No Previous Meme Exists !", Toast.LENGTH_SHORT).show()
                }
            }
        }

        share_btn.setOnClickListener {
//            shareImage()
            shareUrl()
        }

    }


    private fun shareImage() {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())


    }

    fun loadmeme() {
        memeprogressbar.visibility = View.VISIBLE
            val queue = Volley.newRequestQueue(this)
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, URL, null,
                { response ->
                    val meme_url = response.getString("url").toString()
                    Curl = meme_url
                    Curl_textview.setText(Curl)
                    Glide.with(this).load(meme_url).listener(object: RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            memeprogressbar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            memeprogressbar.visibility = View.GONE
                            return false

                        }

                    }).into(meme_img)
                },
                { error ->
                    Toast.makeText(this, "Some thing went wrong !", Toast.LENGTH_SHORT).show()
                })
            queue.add(jsonObjectRequest)

        }

    fun shareUrl(){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plane"
        intent.putExtra(Intent.EXTRA_TEXT,"Bhai ye wala meme dekh ${Curl_textview.text}")
        val chooser = Intent.createChooser(intent,"Share On ... ")
        startActivity(chooser)
    }
}