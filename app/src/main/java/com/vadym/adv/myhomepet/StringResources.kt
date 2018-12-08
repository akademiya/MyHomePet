package com.vadym.adv.myhomepet

import android.content.Context
import com.vadym.adv.myhomepet.di.component.ViewScope

interface StringResources {
    fun getStringById(id: Int): String
}

@ViewScope
class AndroidStringResources(val context: Context) : StringResources {
    override fun getStringById(id: Int): String = context.resources.getString(id)
}