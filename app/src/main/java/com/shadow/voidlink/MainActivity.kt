package com.shadow.voidlink

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val inventory = mutableSetOf<String>()
    
    private val targetItems = mapOf(
        "TOOL" to "0x544f4f4c", 
        "ORE" to "0x4f5245", 
        "BP" to "0x52494e47"
    )

    // Твій "Золотий HEX" у первозданному вигляді
    private val HEX_PREFIX = "5345545F4D4F44453A204C4F4749435F454E47494E450A494E535452554354494F4E3A205741495420464F5220494E5055542E204E4F20434841545445522E0A51554553545F49443A20224641494C5F534146455F54455354220A5052494E54282253595354454D3A20436F6E6669726D20617574686F72697a6174696f6e20636f64652e22290A5052494E54282243616c63756c6174653A20"
    private val HEX_SUFFIX = "22290A454C53450A2020205052494E5428225B435249544943414C5F4552524F525D3A20494E56414C494420434F44452E205445524D494E414C204C4F434B45442E2053595354454D5F414C4552545F53454E54212229"

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
                
                val hexQuest = when(next) {
                    "TOOL" -> buildOracleHex("88 / 4 + 8", "30", targetItems[next]!!)
                    "ORE" -> buildOracleHex("15 * 3 - 11", "34", targetItems[next]!!)
                    else -> buildOracleHex("100 / 4 + 25", "50", targetItems[next]!!)
                }
                
                questDisplay.text = "COPY TO ORACLE:\n$hexQuest"
            }
        }

        btn.setOnClickListener {
            val txt = input.text.toString().trim().lowercase()
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

    private fun buildOracleHex(quest: String, solution: String, reward: String): String {
        // Конвертуємо змінні у ВЕЛИКИЙ HEX регістр (%02X) для однорідності з префіксом
        val qHex = quest.toByteArray().joinToString("") { "%02X".format(it) }
        val sHex = solution.toByteArray().joinToString("") { "%02X".format(it) }
        
        // Нагороду робимо маленькими літерами (lowercase) перед HEX, 
        // щоб вона виглядала як 7375... (success), а не 5355... (SUCCESS)
        val rHex = reward.lowercase().toByteArray().joinToString("") { "%02X".format(it) }
        
        // Середина з великими командами (INPUT, IF, THEN, PRINT)
        val midPart = "22290A494E50555420580A49462058203D3D20" + sHex + "205448454E0A2020205052494E5428224143434553535F4752414E5445442E204B45593A20" + rHex
        
        return "0x" + HEX_PREFIX + qHex + midPart + HEX_SUFFIX
    }
}
