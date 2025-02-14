package ir.kasebvatan.ktor_todo

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import ir.kasebvatan.ktor_todo.databinding.SheetNoteBinding
import ir.kasebvatan.ktor_todo.helper.Cache
import ir.kasebvatan.ktor_todo.helper.Helper
import ir.kasebvatan.ktor_todo.model.TaskModel

class NoteSheet(
    context: Context,
    private val model: TaskModel?,
    private val callback: (TaskModel, String) -> Unit
) : BottomSheetDialog(context) {

    private lateinit var binding: SheetNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SheetNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            if (model != null) {
                inputTitle.setText(model.title)
                inputContent.setText(model.content)
                txtEdit.text = "EDIT"
            } else {
                txtEdit.text = "SAVE"
            }


            txtBack.setOnClickListener { dismiss() }

            txtEdit.setOnClickListener {
                if (inputTitle.text.toString().isEmpty()) inputTitle.error = "FILL"
                else if (inputContent.text.toString().isEmpty()) inputContent.error = "FILL"
                else {
                    dismiss()
                    if (model == null) {
                        callback.invoke(
                            TaskModel(
                                userId = Cache["userId"],
                                title = inputTitle.text.toString(),
                                content = inputContent.text.toString(),
                                lastModifiedDate = Helper.now(),
                                id = -1
                            ), "save"
                        )
                    } else {
                        callback.invoke(
                            model.apply {
                                title = inputTitle.text.toString()
                                content = inputContent.text.toString()
                                lastModifiedDate = Helper.now()
                            }, "edit"
                        )
                    }
                }

            }


        }

    }

}