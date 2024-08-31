package com.demoapp

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.demoapp.adapter.StudentAdapter
import com.demoapp.base.BaseActivity
import com.demoapp.databinding.ActivityMainBinding
import com.demoapp.model.Student
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private var behavior: BottomSheetBehavior<*>? = null
    private lateinit var studentAdapter: StudentAdapter

    private lateinit var students: MutableList<Student>
    private val selectedStudentList = mutableListOf<Student>()
    private val selectedStudentsForBottomSheet = mutableListOf<Student>()

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    @SuppressLint("SetTextI18n")
    override fun setupViews() {
        val bottomSheet = binding.includeBottomSheet.llBottomSheet
        behavior = BottomSheetBehavior.from(bottomSheet)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (behavior?.state == BottomSheetBehavior.STATE_EXPANDED ||
                    behavior?.state == BottomSheetBehavior.STATE_HALF_EXPANDED) {
                    // Close the bottom sheet if it is open
                    behavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                } else {
                    if (isEnabled) {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        })

        val searchBox = binding.includeBottomSheet.searchBox
        val clearIcon = binding.includeBottomSheet.clearIcon
        val doneButton = binding.includeBottomSheet.txtDone
        val cancelButton = binding.includeBottomSheet.txtCancel

        behavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    binding.backdropShadow.visibility = View.VISIBLE
                    updateBottomSheetSelection()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.backdropShadow.alpha =
                    slideOffset * 0.5f  // Adjust alpha based on slideOffset
            }
        })

        binding.btnSelectStudent.setOnClickListener {
            if (behavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
                behavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                binding.includeBottomSheet.txtClassName.text = "Class IV A"
                behavior?.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        setupRecyclerView()

        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Show clear icon if there is text, otherwise hide it
                clearIcon.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE

                // Apply the filter to the adapter based on the search query
                studentAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
                checkIfRecyclerViewIsEmpty()
            }
        })

        clearIcon.setOnClickListener {
            searchBox.text.clear()
            studentAdapter.filter.filter("")
        }

        doneButton.isEnabled = false

        doneButton.setOnClickListener {
            selectedStudentsForBottomSheet.clear()
            selectedStudentsForBottomSheet.addAll(selectedStudentList)

            binding.txtStudentCount.text = "Selected Students: ${selectedStudentList.size}"
            behavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        cancelButton.setOnClickListener {
            behavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        updateCount()
    }

    private fun setupRecyclerView() {
        students = listOf(
            Student(1, "John Doe", "https://via.placeholder.com/150", false),
            Student(2, "Jane Smith", "https://via.placeholder.com/150", false),
            Student(3, "Michael Johnson", "https://via.placeholder.com/150", false),
            Student(4, "Emily Davis", "https://via.placeholder.com/150", false),
            Student(5, "Daniel Brown", "https://via.placeholder.com/150", false),
            Student(6, "Sophia Wilson", "https://via.placeholder.com/150", false),
            Student(7, "Liam Miller", "https://via.placeholder.com/150", false),
            Student(8, "Olivia Moore", "https://via.placeholder.com/150", false),
            Student(9, "Noah Taylor", "https://via.placeholder.com/150", false),
            Student(10, "Ava Anderson", "https://via.placeholder.com/150", false),
            Student(11, "Elijah Thomas", "https://via.placeholder.com/150", false),
            Student(12, "Isabella Jackson", "https://via.placeholder.com/150", false),
            Student(13, "James White", "https://via.placeholder.com/150", false),
            Student(14, "Mia Harris", "https://via.placeholder.com/150", false),
            Student(15, "Benjamin Martin", "https://via.placeholder.com/150", false),
            Student(16, "Amelia Thompson", "https://via.placeholder.com/150", false),
            Student(17, "Lucas Garcia", "https://via.placeholder.com/150", false),
            Student(18, "Charlotte Martinez", "https://via.placeholder.com/150", false),
            Student(19, "Henry Robinson", "https://via.placeholder.com/150", false),
            Student(20, "Evelyn Clark", "https://via.placeholder.com/150", false),
            Student(21, "Alexander Rodriguez", "https://via.placeholder.com/150", false),
            Student(22, "Abigail Lewis", "https://via.placeholder.com/150", false),
            Student(23, "Mason Lee", "https://via.placeholder.com/150", false),
            Student(24, "Scarlett Walker", "https://via.placeholder.com/150", false),
            Student(25, "Sebastian Hall", "https://via.placeholder.com/150", false),
            Student(26, "Victoria Allen", "https://via.placeholder.com/150", false),
            Student(27, "Jack Young", "https://via.placeholder.com/150", false),
            Student(28, "Harper King", "https://via.placeholder.com/150", false),
            Student(29, "Jackson Wright", "https://via.placeholder.com/150", false),
            Student(30, "Ella Scott", "https://via.placeholder.com/150", false),
            Student(31, "Aiden Green", "https://via.placeholder.com/150", false),
            Student(32, "Grace Adams", "https://via.placeholder.com/150", false),
            Student(33, "Samuel Baker", "https://via.placeholder.com/150", false),
            Student(34, "Aria Gonzalez", "https://via.placeholder.com/150", false),
            Student(35, "David Nelson", "https://via.placeholder.com/150", false),
            Student(36, "Chloe Carter", "https://via.placeholder.com/150", false),
            Student(37, "Joseph Mitchell", "https://via.placeholder.com/150", false),
            Student(38, "Lily Perez", "https://via.placeholder.com/150", false),
            Student(39, "Matthew Roberts", "https://via.placeholder.com/150", false),
            Student(40, "Zoe Turner", "https://via.placeholder.com/150", false),
            Student(41, "Owen Phillips", "https://via.placeholder.com/150", false),
            Student(42, "Penelope Campbell", "https://via.placeholder.com/150", false),
            Student(43, "Leo Parker", "https://via.placeholder.com/150", false),
            Student(44, "Hannah Evans", "https://via.placeholder.com/150", false),
            Student(45, "Isaac Edwards", "https://via.placeholder.com/150", false),
            Student(46, "Nora Collins", "https://via.placeholder.com/150", false),
            Student(47, "Gabriel Stewart", "https://via.placeholder.com/150", false),
            Student(48, "Addison Morris", "https://via.placeholder.com/150", false),
            Student(49, "Wyatt Rogers", "https://via.placeholder.com/150", false),
            Student(50, "Aubrey Reed", "https://via.placeholder.com/150", false)
        ).toMutableList()

        studentAdapter = StudentAdapter(students) { student, isChecked ->
            if (isChecked) selectedStudentList.add(student) else selectedStudentList.remove(student)
            updateCount()
            checkIfRecyclerViewIsEmpty()
        }
        binding.includeBottomSheet.recyclerView.apply {
            adapter = studentAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateBottomSheetSelection() {
        selectedStudentList.clear()
        selectedStudentList.addAll(selectedStudentsForBottomSheet)

        studentAdapter.notifyDataSetChanged()
        updateCount()
    }

    @SuppressLint("SetTextI18n")
    private fun updateCount() {
        val selectedCount = selectedStudentList.size
        binding.includeBottomSheet.txtCount.text = "$selectedCount/${students.size} Selected"

        val isEnabled = selectedCount > 0
        binding.includeBottomSheet.txtDone.isEnabled = isEnabled
        updateButtonOpacity(binding.includeBottomSheet.txtDone, isEnabled)
    }

    private fun updateButtonOpacity(button: TextView, enabled: Boolean) {
        button.alpha = if (enabled) 1.0f else 0.5f
    }

    private fun checkIfRecyclerViewIsEmpty() {
        val isEmpty = studentAdapter.itemCount == 0
        binding.includeBottomSheet.recyclerView.visibility =
            if (isEmpty) View.GONE else View.VISIBLE
        binding.includeBottomSheet.emptyView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}
