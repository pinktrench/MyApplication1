package com.example.myapplication

import android.content.Context
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    private var isMenuOpen = false
    private var currentMealType: String? = null // Для отслеживания выбранного типа еды

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fabMenu = findViewById<LinearLayout>(R.id.fabMenu)
        val fab = findViewById<Button>(R.id.fab)
        val foodInputLayout = findViewById<LinearLayout>(R.id.foodInputLayout)
        val foodInput = findViewById<EditText>(R.id.foodInput)
        val doneButton = findViewById<Button>(R.id.doneButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        val breakfastButton = findViewById<Button>(R.id.breakfastButton)
        val lunchButton = findViewById<Button>(R.id.lunchButton)
        val dinnerButton = findViewById<Button>(R.id.dinnerButton)

        val breakfastItems = findViewById<LinearLayout>(R.id.breakfastItems)
        val lunchItems = findViewById<LinearLayout>(R.id.lunchItems)
        val dinnerItems = findViewById<LinearLayout>(R.id.dinnerItems)

        // Настройка системных вставок (insets)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Закрытие меню и возврат крестика
        fun closeFabMenu(fab: Button, fabMenu: LinearLayout) {
            fabMenu.visibility = View.GONE
            fab.text = "+" // Возврат крестика в плюс
        }

        // Логика открытия/закрытия меню
        fab.setOnClickListener {
            if (fabMenu.visibility == View.GONE) {
                fabMenu.visibility = View.VISIBLE
                fab.text = "x" // Замена плюса на крестик
            } else {
                closeFabMenu(fab, fabMenu) // Закрываем меню
            }
        }




        // Открытие меню ввода пищи
        fun openFoodInputMenu() {
            foodInputLayout.visibility = View.VISIBLE
            fabMenu.visibility = View.GONE // Скрываем меню плюса
        }

        // Скрытие клавиатуры
        fun hideKeyboard() {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        // Скрытие меню ввода пищи и возврат интерфейса
        fun hideFoodInputMenu() {
            foodInputLayout.visibility = View.GONE
            hideKeyboard()
            closeFabMenu(fab, fabMenu)
        }

        // Обработка нажатий на кнопки "Завтрак", "Обед", "Ужин"
        breakfastButton.setOnClickListener {
            currentMealType = "breakfast"
            openFoodInputMenu()
        }

        lunchButton.setOnClickListener {
            currentMealType = "lunch"
            openFoodInputMenu()
        }

        dinnerButton.setOnClickListener {
            currentMealType = "dinner"
            openFoodInputMenu()
        }

        // Обработка кнопки "Готово"
        doneButton.setOnClickListener {
            val inputText = foodInput.text.toString()
            if (inputText.isNotEmpty()) {
                when (currentMealType) {
                    "breakfast" -> addItemToMeal(breakfastItems, inputText)
                    "lunch" -> addItemToMeal(lunchItems, inputText)
                    "dinner" -> addItemToMeal(dinnerItems, inputText)
                }
                foodInput.text.clear() // Очистить поле ввода после добавления
                hideFoodInputMenu() // Скрыть поле ввода и вернуть меню плюса
            }
        }

        // Обработка кнопки "Отмена"
        cancelButton.setOnClickListener {
            hideFoodInputMenu()
        }
    }

    // Функция для добавления элемента в соответствующий раздел
    private fun addItemToMeal(mealSection: LinearLayout, itemText: String) {
        val newItem = TextView(this)
        newItem.text = itemText
        newItem.textSize = 16f
        mealSection.addView(newItem)
    }

    // Анимация поворота кнопки (если нужно)
    private fun rotateFab(button: Button, startText: String, endText: String) {
        val rotate = ObjectAnimator.ofFloat(button, "rotation", 0f, 180f)
        rotate.duration = 300
        rotate.start()

        rotate.addUpdateListener { animation ->
            val progress = animation.animatedFraction
            if (progress >= 0.5f) {
                button.text = endText
            }
        }

        rotate.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}
            override fun onAnimationEnd(animation: android.animation.Animator) {
                button.rotation = 0f
            }
            override fun onAnimationCancel(animation: android.animation.Animator) {}
            override fun onAnimationRepeat(animation: android.animation.Animator) {}
        })
    }
}
