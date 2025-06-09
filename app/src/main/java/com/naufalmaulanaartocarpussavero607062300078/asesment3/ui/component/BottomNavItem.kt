package com.naufalmaulanaartocarpussavero607062300078.asesment3.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

sealed class BottomNavItem(
    val name: String,
    val route: String
) {
    data class VectorIconItem(
        val navName: String,
        val navRoute: String,
        val icon: ImageVector,
        val selectedIcon: ImageVector? = null
    ) : BottomNavItem(navName, navRoute)

    data class DrawableIconItem(
        val navName: String,
        val navRoute: String,
        val iconResId: Int,
        val selectedIconResId: Int? = null
    ) : BottomNavItem(navName, navRoute)

    @Composable
    fun Icon(isSelected: Boolean = false) {
        when (this) {
            is VectorIconItem -> {
                androidx.compose.material3.Icon(
                    imageVector = if (isSelected && selectedIcon != null) selectedIcon else icon,
                    contentDescription = name
                )
            }
            is DrawableIconItem -> {
                androidx.compose.material3.Icon(
                    painter = painterResource(
                        if (isSelected && selectedIconResId != null) selectedIconResId
                        else iconResId
                    ),
                    contentDescription = name
                )
            }
        }
    }
}