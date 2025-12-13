package com.akshat.rickandmorty.components.character

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akshat.rickandmorty.data.DataPoint
import com.akshat.rickandmorty.ui.theme.Purple40
import com.akshat.rickandmorty.ui.theme.PurpleGrey80

@Composable
fun DataPointComponent( dataPoint: DataPoint){
    Column()
    {
        Text(text = dataPoint.name, fontSize = 14.sp, color = PurpleGrey80)
        Text(text = dataPoint.description, fontSize = 24.sp,
            fontWeight = FontWeight.Bold, color = Purple40 )
    }
}

@Preview
@Composable
fun PreviewDataPointComponent(){
    DataPointComponent(
        dataPoint = DataPoint(
            name = "Last known location",
            description = "At Springfield"))
}