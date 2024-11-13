package com.gerardshen.fetchhiringeval

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gerardshen.fetchhiringeval.ui.theme.FetchHiringEvalTheme
import com.gerardshen.fetchhiringeval.MainActivityViewModel.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.TreeMap

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FetchHiringEvalTheme {
                ItemListScreen()
            }
        }
    }
}

@Composable
fun ListItem(itemName: String) {
    Text(
        text = itemName,
        style = MaterialTheme.typography.headlineLarge
    )
}

@Composable
fun ListSection(sectionName: String,
                isExpanded: Boolean,
                onClicked: () -> Unit) {
    Row(modifier = Modifier
        .clickable { onClicked() }) {
        if (isExpanded) {
            Icon(
                Icons.Rounded.KeyboardArrowUp,
                "Close List Section",
                modifier = Modifier
                    .height(30.dp)
            )
        } else {
            Icon(
                Icons.Rounded.KeyboardArrowDown,
                "Expand List Section",
                modifier = Modifier
                    .height(30.dp)
            )
        }
        Text(
            text = sectionName,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

fun LazyListScope.Section(
    sectionName: String,
    itemList: MutableList<FetchItem>,
    isExpanded: Boolean,
    onHeaderClick: () -> Unit
) {

    item {
        ListSection(
            sectionName = sectionName,
            isExpanded = isExpanded,
            onClicked = onHeaderClick
        )
    }

    if(isExpanded) {
        items(itemList.size) {
            ListItem(itemName = itemList[it].name ?: "")
        }
    }
}

@Composable
fun SectionedList(itemMap: TreeMap<Int, MutableList<FetchItem>>) {
    val sectionExpendedMap = remember {
        List(itemMap.size) {
            i: Int -> i to false
        }.toMutableStateMap()
    }
    LazyColumn(
        content = {
            itemMap.onEachIndexed { index, mapEntry ->
                Section(
                    sectionName = "List ${mapEntry.key}",
                    itemList = mapEntry.value,
                    isExpanded = sectionExpendedMap[index] ?: false,
                    onHeaderClick = {
                        sectionExpendedMap[index] = !(sectionExpendedMap[index] ?: false)
                    }
                )

            }
        }
    )
}

@Composable
fun ItemListScreen() {
    val viewModel: MainActivityViewModel = hiltViewModel()
    viewModel.requestFetchItems()
    val state by viewModel.state.collectAsState()
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .padding(10.dp, 30.dp, 10.dp, 10.dp)) {
        Button(onClick = { viewModel.requestFetchItems() },
            modifier = Modifier.fillMaxWidth()) {
            Text(
                "Request Items",
                style = MaterialTheme.typography.headlineLarge
            )
        }
        when (state) {
            is MainScreenState.Loading -> {
                Text(
                    text = "Loading",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is MainScreenState.Error -> {
                Text(
                    text = "Error",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            is MainScreenState.Finished -> {
                val map = (state as MainScreenState.Finished).itemMap
                if (null != map) {
                    SectionedList(map)
                } else {
                    Text(
                        text = "Error",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

