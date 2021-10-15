package edu.davidd.weatherlogger.framework.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import edu.davidd.weatherlogger.R
import edu.davidd.weatherlogger.framework.ui.compose.AppColumn
import edu.davidd.weatherlogger.framework.ui.compose.firstBaselineToTop
import edu.davidd.weatherlogger.framework.ui.compose.theme.AppShape
import edu.davidd.weatherlogger.framework.ui.compose.theme.AppTextStyle
import edu.davidd.weatherlogger.framework.ui.compose.theme.WeatherLoggerTheme
import edu.davidd.weatherlogger.framework.ui.model.WeatherItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
internal fun Main(
    weatherItems: List<WeatherItem>,
    uiMessageEvent: UiEvent<UiMessage>?,
    validateLocationPermission: () -> Unit
) {
    Timber.d("Main")
    WeatherLoggerTheme {
        Timber.d("Main - WeatherLoggerTheme")

        val scaffoldState = rememberScaffoldState()
        val coroutineScope = rememberCoroutineScope()
        AppSnackBar(scaffoldState.snackbarHostState, coroutineScope, uiMessageEvent)

        Scaffold(scaffoldState = scaffoldState) { innerPadding ->
            Timber.d("Main - WeatherLoggerTheme - Scaffold")
            MainLayout(
                Modifier.padding(innerPadding),
                weatherItems,
                validateLocationPermission
            )
        }
    }
}

@Composable
fun TestLayout() {
    Box {
//        val lazyListState = rememberLazyListState()
//        val coroutineScope = rememberCoroutineScope()
//
//        coroutineScope.launch {
//            Timber.d("Main - WeatherLoggerTheme - Scaffold - coroutineScope")
//            delay(3000)
//            lazyListState.animateScrollToItem(29)
//            delay(3000)
//            lazyListState.animateScrollToItem(0)
//        }

        val scrollState = rememberScrollState()

        AppColumn(
            Modifier
                .verticalScroll(scrollState)
        ) {
            repeat(10) {
                Image(
                    painter = rememberImagePainter(
                        data = "https://developer.android.com/images/brand/Android_Robot.png"
                    ),
                    contentDescription = "Android Logo",
                    modifier = Modifier
                        .size(50.dp)
                )

                Text(
                    "... $it \n second line \n third line",
                    Modifier.firstBaselineToTop(100.dp)
                )
            }
        }
    }
}

@Composable
private fun MainLayout(
    modifier: Modifier = Modifier,
    weatherItems: List<WeatherItem>,
    validateLocationPermission: () -> Unit
) {
    ConstraintLayout(
        modifier
            .fillMaxSize()
    ) {
        Timber.d("Main - WeatherLoggerTheme - ConstraintLayout")

        val (column, button) = createRefs()

        LazyColumn(
            modifier = Modifier
                .constrainAs(column) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(button.top)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            Timber.d("LazyColumn")
            items(weatherItems) {
                Timber.d("LazyColumn items - $it")
                WeatherRow(it)
            }
        }

        Button(
            shape = AppShape.NoCorner,
            modifier = Modifier
                .constrainAs(button) {
                    top.linkTo(column.bottom)
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.horizontal_margin),
                    vertical = dimensionResource(id = R.dimen.vertical_margin)
                ),
            onClick = validateLocationPermission) {
            Timber.d("Button")
            Text(
                text = stringResource(R.string.load).uppercase(),
                style = AppTextStyle.BigButton
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun WeatherRow(item: WeatherItem = WeatherItem("2021.10.01", "26 C", "Brussels")) {
    Timber.d("WeatherRow - $item")
    Card(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.margin))
            .fillMaxSize()
            .focusable(true),
        elevation = 8.dp,
        shape = AppShape.Corner
    ) {
        Timber.d("WeatherRow - $item - Card")
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.margin))
        ) {
            Timber.d("WeatherRow - $item - Card - ConstraintLayout")
            val (temperatureLabel, temperature, date, location) = createRefs()

            Text(
                text = stringResource(R.string.temperature),
                modifier = Modifier
                    .constrainAs(temperatureLabel) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .padding(horizontal = dimensionResource(R.dimen.margin_50), vertical = dimensionResource(R.dimen.margin_50)),
                style = MaterialTheme.typography.body1,
                maxLines = 2
            )
            Text(
                text = item.temperature,
                modifier = Modifier
                    .constrainAs(temperature) {
                        top.linkTo(parent.top)
                        start.linkTo(temperatureLabel.end)
                    }
                    .padding(vertical = dimensionResource(R.dimen.margin_50)),
                style = MaterialTheme.typography.body1,
                maxLines = 2
            )
            Text(
                text = item.date,
                modifier = Modifier
                    .constrainAs(date) {
                        start.linkTo(parent.start)
                        top.linkTo(temperatureLabel.bottom)
                    }
                    .padding(horizontal = dimensionResource(R.dimen.margin_50), vertical = dimensionResource(R.dimen.margin_50)),
                style = MaterialTheme.typography.body1,
                maxLines = 2
            )
            Text(
                text = item.location,
                modifier = Modifier
                    .constrainAs(location) {
                        end.linkTo(parent.end)
                        top.linkTo(date.top)
                    }
                    .padding(horizontal = dimensionResource(R.dimen.margin_50), vertical = dimensionResource(R.dimen.margin_50)),
                style = MaterialTheme.typography.body1,
                maxLines = 2
            )
        }
    }
}

@Preview
@Composable
fun WeatherRow2(item: WeatherItem = WeatherItem("2021.10.01", "26 C", "Brussels")) {
    Timber.d("WeatherRow2 - $item")

    Card(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.margin))
            .fillMaxWidth()
            .focusable(true),
        elevation = 8.dp,
        shape = AppShape.Corner
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.margin))
        ) {
            Row {
                Text(
                    text = stringResource(R.string.temperature),
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.margin_50), vertical = dimensionResource(R.dimen.margin_50)),
                    style = MaterialTheme.typography.body1,
                    maxLines = 2
                )
                Text(
                    text = item.temperature,
                    modifier = Modifier
                        .padding(vertical = dimensionResource(R.dimen.margin_50)),
                    style = MaterialTheme.typography.body1,
                    maxLines = 2
                )
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = item.date,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(horizontal = dimensionResource(R.dimen.margin_50), vertical = dimensionResource(R.dimen.margin_50)),
                    style = MaterialTheme.typography.body1,
                    maxLines = 2
                )

                Text(
                    text = item.location,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(horizontal = dimensionResource(R.dimen.margin_50), vertical = dimensionResource(R.dimen.margin_50)),
                    style = MaterialTheme.typography.body1,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun AppToast(uiMessage: UiMessage?) {
    Timber.d("AppToast - $uiMessage")
    uiMessage?.let {
        Toast.makeText(
            LocalContext.current,
            it.resId,
            Toast.LENGTH_LONG
        ).show()
    }
}

@Composable
fun AppSnackBar(snackbarHostState: SnackbarHostState, coroutineScope: CoroutineScope, uiMessageEvent: UiEvent<UiMessage>?) {
    Timber.d("AppSnackBar - $uiMessageEvent")

    uiMessageEvent?.getContentIfNotHandled()?.let {
        val message = stringResource(it.resId)
        val actionLabel = it.action?.let { stringResource(it.actionResId) }
        val context = LocalContext.current

        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel
            ).let { result ->
                if (result == SnackbarResult.ActionPerformed) it.action?.runnable?.invoke(context)
            }
        }
    }
}