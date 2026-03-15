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
                status.text = "SUCCESS: System Stable"
                questDisplay.text = "SHADOW RING ACTIVE"
            } else {
                val next = missing.first()
                status.text = "REQUIRED: $next"
                
                // Формуємо рядок вручну без зайвих відступів trimIndent, щоб HEX був чистим
                val sb = StringBuilder()
                sb.append("SET_MODE: LOGIC_ENGINE\n")
                sb.append("INSTRUCTION: WAIT FOR INPUT. NO CHATTER.\n")
                sb.append("QUEST_ID: \"VOID_LINK_RECOVERY\"\n")
                sb.append("PRINT(\"SYSTEM: Confirm authorization code.\")\n")
                sb.append("PRINT(\"Calculate: 15 * 3 - 38\")\n")
                sb.append("INPUT X\n")
                sb.append("IF X == 7 THEN\n")
                sb.append("   PRINT(\"ACCESS_GRANTED. KEY: ${targetItems[next]}\")\n")
                sb.append("ELSE\n")
                sb.append("   PRINT(\"[CRITICAL_ERROR]: INVALID CODE. TERMINAL LOCKED. SYSTEM_ALERT_SENT!\")")

                val instruction = sb.toString()
                val q = "0x" + instruction.toByteArray(Charsets.US_ASCII).joinToString(""){ "%02X".format(it) }
                questDisplay.text = "COPY TO ORACLE:\n$q"
            }
        }

        btn.setOnClickListener {
            val txt = input.text.toString().trim().uppercase()
            if (targetItems.values.contains(txt)) {
                val componentName = targetItems.filterValues { it == txt }.keys.first()
                if (inventory.add(componentName)) {
                    Toast.makeText(this, "Success: $componentName", Toast.LENGTH_SHORT).show()
                }
                input.setText("")
                updateUI()
            } else {
                Toast.makeText(this, "INVALID KEY", Toast.LENGTH_SHORT).show()
            }
        }
        updateUI()
    }
}
