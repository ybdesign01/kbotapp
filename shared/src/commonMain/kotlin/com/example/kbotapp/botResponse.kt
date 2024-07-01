package com.example.kbotapp

object botResponse {

    fun getResponse(msg : String):String{
        val list = listOf("Resoudre une équation (Ecrivez l'équation)","Ouvrir Google", "Rechercher <terme>" )
        val random = (0..2).random()
        val message = msg.lowercase()
        return when{
            message.contains("bonjour") ->{
                when(random){
                    0-> "Bonjour! ça va?"
                    1->"Salut, comment allez-vous?"
                    2->"Salam Aleikoum"
                    else -> {
                        "Erreur interne."
                    }
                }
            }
            message.contains("ca va") && message.contains("et")->{
                when(random){
                    0-> "Hamdoulah, merci"
                    1->"Je vais bien, merci"
                    2->"Je suis en pleine forme, merci"
                    else -> {
                        "Erreur interne."
                    }
                }
            }
            message.equals("ca va")->{
                when(random){
                    0-> "Hamdoulah, et vous?"
                    1->"Je vais bien, et vous?"
                    2->"Je suis en pleine forme, et vous?"
                    else -> {
                        "Erreur interne."
                    }
                }
            }
            message.contains("resoudre") ->{
               val a : String? = message.substringAfter("resoudre")
                return try{
                    val res = mathResponse.solve(a?:"0")
                    res.toString()
                }catch(e : Exception){
                    "Je ne peux pas resoudre cette equation."
                }
            }
            message.contains("rechercher") ->{
                "Recherche..."
            }
            message.contains("aide") ->{
                "Voici mes commandes: \nRésoudre <equation>\nOuvrir Google\nRechercher <terme>\nOuvrir <application>\n"+
                        "Appeler <numero>\nMail <mail@mail.com>"
            }

            message.contains("ouvrir")->{
                "Ouverture..."
            }
            message.contains("appeler")->{
                "Appel..."
            }
            message.contains("mail")->{
                "Envoi..."
            }
            else -> {
                when(random){
                    0->"Je ne comprends pas."
                    1->"Quoi?"
                    2->"Pouvez vous m'expliquer plus?"
                    else -> {
                        "Erreur interne."
                    }
                }
            }
        }

    }

}