package com.alvaroquintana.edadperruna.wear.tile

import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ColorBuilders.argb
import androidx.wear.protolayout.DimensionBuilders.dp
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.DimensionBuilders.sp
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.Box
import androidx.wear.protolayout.LayoutElementBuilders.Column
import androidx.wear.protolayout.LayoutElementBuilders.FontStyle
import androidx.wear.protolayout.LayoutElementBuilders.Layout
import androidx.wear.protolayout.LayoutElementBuilders.Spacer
import androidx.wear.protolayout.LayoutElementBuilders.Text
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.ModifiersBuilders.Modifiers
import androidx.wear.protolayout.ModifiersBuilders.Padding
import androidx.wear.protolayout.ModifiersBuilders.Semantics
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileService
import com.alvaroquintana.edadperruna.wear.WearMainActivity
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

private const val RESOURCES_VERSION = "1"
private const val ID_LAUNCH_CALCULATOR = "launch_calculator"

// Brand palette mirrors PerrunoWearColor (dark theme defaults — Tiles ignore the
// system theme of the activity, they get their own color resolver from SysUI).
private const val GOLDEN_PAWS_PRIMARY = 0xFFE3B341.toInt()
private const val GOLDEN_PAWS_ON_SURFACE = 0xFFEDE7DC.toInt()
private const val GOLDEN_PAWS_ON_SURFACE_VARIANT = 0xFFB8B0A2.toInt()

class PerrunoTileService : TileService() {

    override fun onTileRequest(
        requestParams: RequestBuilders.TileRequest,
    ): ListenableFuture<TileBuilders.Tile> {
        val tile = TileBuilders.Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(
                TimelineBuilders.Timeline.Builder()
                    .addTimelineEntry(
                        TimelineBuilders.TimelineEntry.Builder()
                            .setLayout(
                                Layout.Builder()
                                    .setRoot(buildTileLayout())
                                    .build(),
                            )
                            .build(),
                    )
                    .build(),
            )
            .build()
        return Futures.immediateFuture(tile)
    }

    override fun onTileResourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest,
    ): ListenableFuture<ResourceBuilders.Resources> {
        val resources = ResourceBuilders.Resources.Builder()
            .setVersion(RESOURCES_VERSION)
            .build()
        return Futures.immediateFuture(resources)
    }

    private fun buildTileLayout(): LayoutElementBuilders.LayoutElement {
        val launchAction = ActionBuilders.LaunchAction.Builder()
            .setAndroidActivity(
                ActionBuilders.AndroidActivity.Builder()
                    .setPackageName(packageName)
                    .setClassName(WearMainActivity::class.java.name)
                    .build(),
            )
            .build()

        val clickable = Clickable.Builder()
            .setId(ID_LAUNCH_CALCULATOR)
            .setOnClick(launchAction)
            .build()

        val column = Column.Builder()
            .setHorizontalAlignment(LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER)
            .addContent(
                Text.Builder()
                    .setText("🐾")
                    .setFontStyle(
                        FontStyle.Builder()
                            .setSize(sp(36f))
                            .setColor(argb(GOLDEN_PAWS_PRIMARY))
                            .build(),
                    )
                    .build(),
            )
            .addContent(Spacer.Builder().setHeight(dp(6f)).build())
            .addContent(
                Text.Builder()
                    .setText("Edad Perruna")
                    .setFontStyle(
                        FontStyle.Builder()
                            .setSize(sp(15f))
                            .setWeight(LayoutElementBuilders.FONT_WEIGHT_BOLD)
                            .setColor(argb(GOLDEN_PAWS_ON_SURFACE))
                            .build(),
                    )
                    .build(),
            )
            .addContent(Spacer.Builder().setHeight(dp(2f)).build())
            .addContent(
                Text.Builder()
                    .setText("Toca para calcular")
                    .setFontStyle(
                        FontStyle.Builder()
                            .setSize(sp(11f))
                            .setColor(argb(GOLDEN_PAWS_ON_SURFACE_VARIANT))
                            .build(),
                    )
                    .build(),
            )
            .build()

        return Box.Builder()
            .setWidth(expand())
            .setHeight(expand())
            .setVerticalAlignment(LayoutElementBuilders.VERTICAL_ALIGN_CENTER)
            .setHorizontalAlignment(LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER)
            .setModifiers(
                Modifiers.Builder()
                    .setClickable(clickable)
                    .setPadding(
                        Padding.Builder()
                            .setAll(dp(12f))
                            .build(),
                    )
                    .setSemantics(
                        Semantics.Builder()
                            .setContentDescription(
                                "Edad Perruna. Toca para abrir la calculadora",
                            )
                            .build(),
                    )
                    .build(),
            )
            .addContent(column)
            .build()
    }
}

