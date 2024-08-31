package com.demoapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.demoapp.databinding.ListItemStudentBinding
import com.demoapp.model.Student
import dpToPx

class StudentAdapter(
    private val students: List<Student>,
    private val onStudentChecked: (Student, Boolean) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>(), Filterable {

    private var filteredStudents: List<Student> = students.toList() // Ensure filtered list is initialized

    inner class StudentViewHolder(private val binding: ListItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(student: Student, position: Int) {
            binding.txtStudentName.text = student.title

            // Adjust bottom margin and divider visibility
            if (position == filteredStudents.size - 1) {
                binding.horizontalDivider.visibility = View.GONE
                val params = binding.llStudent.layoutParams as ViewGroup.MarginLayoutParams
                params.bottomMargin = 16.dpToPx(binding.llStudent.context)
                binding.llStudent.layoutParams = params
            } else {
                binding.horizontalDivider.visibility = View.VISIBLE
                val params = binding.llStudent.layoutParams as ViewGroup.MarginLayoutParams
                params.bottomMargin = 0
                binding.llStudent.layoutParams = params
            }

            // Load the student image using Glide
            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(64))
            Glide.with(binding.imgStudent.context)
                .load(student.image)
                .apply(requestOptions)
                .into(binding.imgStudent)

            // Ensure the checkbox state is correctly reflected based on the student's isChecked property
            binding.cbStudent.setOnCheckedChangeListener(null) // Unbind the listener first
            binding.cbStudent.isChecked = student.isChecked

            // Update the selection when the checkbox state changes
            binding.cbStudent.setOnCheckedChangeListener { _, isChecked ->
                student.isChecked = isChecked // Update the student's checked state
                onStudentChecked(student, isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ListItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(filteredStudents[position], position)
    }

    override fun getItemCount(): Int = filteredStudents.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase() ?: ""
                filteredStudents = if (query.isEmpty()) {
                    students
                } else {
                    students.filter {
                        it.title.lowercase().contains(query)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredStudents
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredStudents = results?.values as List<Student>
                notifyDataSetChanged()
            }
        }
    }
}
