package com.demoapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demoapp.databinding.ListItemStudentBinding
import com.demoapp.model.Student

class StudentAdapter(private val students: List<Student>, private val onSelectionChanged: () -> Unit) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>(), Filterable {

    private var filteredStudents: List<Student> = students

    inner class StudentViewHolder(private val binding: ListItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(student: Student) {
            binding.txtStudentName.text = student.title
            Glide.with(binding.imgStudent.context)
                .load(student.image)
                .into(binding.imgStudent)

            binding.cbStudent.isChecked = student.isChecked
            binding.cbStudent.setOnCheckedChangeListener { _, isChecked ->
                student.isChecked = isChecked
                onSelectionChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ListItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(filteredStudents[position])
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