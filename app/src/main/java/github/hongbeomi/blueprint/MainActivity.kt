package github.hongbeomi.blueprint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import github.hongbeomi.library.InspectorDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootView = findViewById<ConstraintLayout>(R.id.constraintLayout_root_sample)
        val button = findViewById<Button>(R.id.button_sample)

        button.setOnClickListener {
            val inspectorDialog = InspectorDialog(this, rootView)
            inspectorDialog.show()
        }
    }

}