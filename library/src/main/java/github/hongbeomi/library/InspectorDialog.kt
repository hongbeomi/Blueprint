package github.hongbeomi.library

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import github.hongbeomi.library.databinding.DialogInspectorBinding

class InspectorDialog(
    context: Context,
    private val rootView: ViewGroup
) : Dialog(context, R.style.full_screen_dialog) {

    private val finder = ViewFinder()

    init {
        window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window?.setDimAmount(0.9f)
    }

    private var _binding: DialogInspectorBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DialogInspectorBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        finder.find(rootView, binding.inspectorViewDialog)

        binding.imageButtonInspectorDialogClose.setOnClickListener {
            dismiss()
        }
    }

    override fun dismiss() {
        _binding = null
        super.dismiss()
    }

}