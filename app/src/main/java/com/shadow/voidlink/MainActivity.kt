package com.shadow.voidlink

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val inventory = mutableSetOf<String>()
    private val targetItems = mapOf(
        "TOOL" to "0x544F4F4C", 
        "ORE" to "0x4F5245", 
        "BP" to "0x52494E47"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val status = findViewById<TextView>(R.id.statusText)
        val questDisplay = findViewById<TextView>(R.id.questHex)
        val input = findViewById<EditText>(R.id.hexInput)
        val btn = findViewById<Button>(R.id.btnAction)

        fun updateUI() {
            val missing = targetItems.keys.filter { it !in inventory }
            if (missing.isEmpty()) {
                status.text = "УСПІХ: Кільце Тіні створено!"
                questDisplay.text = "СИСТЕМА СТАБІЛЬНА"
            } else {
                val next = missing.first()
                status.text = "БРАКУЄ: $next"
                // Генеруємо HEX-запит для Оракула
                val q = "0x" + "SET MODE: LOGIC_ENGINE\nPRINT(\"Потрібно: $next\")\nINPUT X\nIF X==7 THEN PRINT(\"0x${targetItems[next]}\")".toByteArray().joinToString(""){ "%02X".format(it) }
                questDisplay.text = "СКОПІЮЙ ЦЕ ОРАКУЛУ:\n$q"
            }
        }

        btn.setOnClickListener {
            val txt = input.text.toString().trim().uppercase()
            if (targetItems.values.contains(txt)) {
                inventory.add(targetItems.filterValues { it == txt }.keys.first())
                Toast.makeText(this, "Компонент додано!", Toast.LENGTH_SHORT).show()
                input.setText("")
                updateUI()
            } else {
                Toast.makeText(this, "Код невірний", Toast.LENGTH_SHORT).show()
            }
        }
        updateUI()
    }
}
