package github.hongbeomi.blueprint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import github.hongbeomi.library.InspectorDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample1)

        val rootView = findViewById<ConstraintLayout>(R.id.constraintLayout_root_sample1)
        val button = findViewById<Button>(R.id.button_sample1)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_sample1)

        recyclerView.adapter = SampleRecyclerViewAdapter()
        button.setOnClickListener {
            val inspectorDialog = InspectorDialog(this, rootView)
            inspectorDialog.show()
        }
    }

}