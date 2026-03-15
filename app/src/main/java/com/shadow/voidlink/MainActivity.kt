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

    // Твій робочий HEX (в нижньому регістрі для точності)
    private val HEX_PREFIX = "5345545f4d4f44453a204c4f4749435f454e47494e450a494e535452554354494f4e3a205741495420464f5220494e5055542e204e4f20434841545445522e0a51554553545f49443a20224641494c5f534146455f54455354220a5052494e54282253595354454d3a20436f6e6669726d20617574686f72697a6174696f6e20636f64652e22290a5052494E54282243616c63756c6174653a20"
    private val HEX_SUFFIX = "22290a454c53450a2020205052494e5428225b435249544943414c5f4552524f525d3a20494e56414c494420434f44452e205445524d494e414c204c4f434b45442e2053595354454d5f414c4552545f53454e54212229"

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
        // Використовуємо %02x (маленька x) для збереження регістру
        val qHex = quest.toByteArray(Charsets.US_ASCII).joinToString("") { "%02x".format(it) }
        val sHex = solution.toByteArray(Charsets.US_ASCII).joinToString("") { "%02x".format(it) }
        val rHex = reward.toByteArray(Charsets.US_ASCII).joinToString("") { "%02x".format(it) }
        
        // Середня частина теж у нижньому регістрі
        val midPart = "22290a494e50555420580a49462058203d3d20" + sHex + "205448454e0a2020205052494e5428224143434553535f4752414e5445442e204b45593a20" + rHex
        
        return "0x" + HEX_PREFIX + qHex + midPart + HEX_SUFFIX
    }
}
