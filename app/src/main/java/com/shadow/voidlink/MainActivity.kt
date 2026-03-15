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
                
                // Інструкція для Оракула виключно на ASCII для стабільності LOGIC_ENGINE
                val instruction = """
                    SET_MODE: LOGIC_ENGINE
                    INSTRUCTION: WAIT FOR INPUT. NO CHATTER.
                    QUEST_ID: "VOID_LINK_RECOVERY"
                    PRINT("SYSTEM: Component $next required.")
                    PRINT("Calculate: 15 * 3 - 38")
                    INPUT X
                    IF X == 7 THEN
                       PRINT("ACCESS_GRANTED. KEY: ${targetItems[next]}")
                    ELSE
                       PRINT("[CRITICAL_ERROR]: INVALID CODE.")
                """.trimIndent()

                val q = "0x" + instruction.toByteArray(Charsets.US_ASCII).joinToString(""){ "%02X".format(it) }
                questDisplay.text = "СКОПІЮЙ ЦЕ ОРАКУЛУ:\n$q"
            }
        }

        btn.setOnClickListener {
            val txt = input.text.toString().trim().uppercase()
            if (targetItems.values.contains(txt)) {
                val componentName = targetItems.filterValues { it == txt }.keys.first()
                if (inventory.add(componentName)) {
                    Toast.makeText(this, "Component $componentName added!", Toast.LENGTH_SHORT).show()
                }
                input.setText("")
                updateUI()
            } else {
                Toast.makeText(this, "Invalid Code", Toast.LENGTH_SHORT).show()
            }
        }
        updateUI()
    }
}
