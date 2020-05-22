package mx.edu.ittepic.ladm_u4_tarea_2_contentprovider_braylosky_ramirez_fletes_20_xx

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var cols = listOf<String>(CallLog.Calls._ID,
                               CallLog.Calls.NUMBER,
                                CallLog.Calls.DURATION,
                                CallLog.Calls.TYPE).toTypedArray()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    llamar.setOnClickListener {
        if(numero.text.isEmpty()){
            Toast.makeText(this,"CAMPOS VACIOS",Toast.LENGTH_LONG).show()
            return@setOnClickListener

        }else{
            permisos()
            numero.setText("")
        }
    }
    if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(this,Array(1){android.Manifest.permission.READ_CALL_LOG},101)
    }else{
        leerLista()
    }

    }

    private fun permisos() {
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE),102)
        }else{
            call()
        }
    }

    @SuppressLint("MissingPermission")
    private fun call() {
        var outgoing = Intent(Intent.ACTION_CALL)

        outgoing.data = Uri.parse("tel:"+numero.text.toString())
        startActivity(outgoing)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 101 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            leerLista()
        };if(requestCode == 102){
            call()
        }
    }

    @SuppressLint("MissingPermission")
    private fun leerLista() {
        var from = listOf<String>(CallLog.Calls.NUMBER,
        CallLog.Calls.DURATION,
        CallLog.Calls.TYPE).toTypedArray()

        var to = intArrayOf(R.id.textView1,R.id.textView2,R.id.textView3)

        var rs = contentResolver.query(CallLog.Calls.CONTENT_URI,cols,null,null,"${CallLog.Calls.LAST_MODIFIED} DESC")

        var adaptador = SimpleCursorAdapter(applicationContext,
                                                R.layout.interfaz_2,
                                                rs,
                                                    from,
                                                    to,0)
        lista.adapter = adaptador
    }
}
