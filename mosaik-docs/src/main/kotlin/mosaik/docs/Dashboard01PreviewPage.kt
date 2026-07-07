package mosaik.docs

import kotlinx.html.FlowContent
import kotlinx.html.HTML
import kotlinx.html.TagConsumer
import kotlinx.html.a
import kotlinx.html.aside
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.head
import kotlinx.html.header
import kotlinx.html.img
import kotlinx.html.li
import kotlinx.html.link
import kotlinx.html.main
import kotlinx.html.meta
import kotlinx.html.p
import kotlinx.html.script
import kotlinx.html.section
import kotlinx.html.stream.appendHTML
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.title
import kotlinx.html.tr
import kotlinx.html.ul
import kotlinx.html.visitAndFinalize
import mosaik.ui.components.ButtonStyle
import mosaik.ui.components.ButtonVariant
import mosaik.ui.components.ButtonWidth
import mosaik.ui.components.CheckboxVariant
import mosaik.ui.components.DrawerBreakpoint
import mosaik.ui.components.DropdownAlignment
import mosaik.ui.components.Size
import mosaik.ui.components.TableSize
import mosaik.ui.components.mAvatar
import mosaik.ui.components.mButton
import mosaik.ui.components.mButtonLink
import mosaik.ui.components.mCard
import mosaik.ui.components.mCardActions
import mosaik.ui.components.mCardBody
import mosaik.ui.components.mCardTitle
import mosaik.ui.components.mCheckbox
import mosaik.ui.components.mDivider
import mosaik.ui.components.mDrawer
import mosaik.ui.components.mDrawerButton
import mosaik.ui.components.mDrawerContent
import mosaik.ui.components.mDrawerSide
import mosaik.ui.components.mDropdown
import mosaik.ui.components.mDropdownContent
import mosaik.ui.components.mDropdownTrigger
import mosaik.ui.components.mJoin
import mosaik.ui.components.mJoinItem
import mosaik.ui.components.mMenu
import mosaik.ui.components.mMenuItem
import mosaik.ui.components.mMenuTitle
import mosaik.ui.components.mStat
import mosaik.ui.components.mStatDesc
import mosaik.ui.components.mStatTitle
import mosaik.ui.components.mStatValue
import mosaik.ui.components.mStats
import mosaik.ui.components.mTable

const val DASHBOARD_01_PREVIEW_PATH = "/blocks/dashboard-01"

private fun <T> TagConsumer<T>.dashboardHtmlDocument(block: HTML.() -> Unit): T =
    HTML(mapOf("data-theme" to DEFAULT_THEME, "lang" to "en"), this, namespace = null)
        .visitAndFinalize(this, block)

fun dashboard01PreviewPage(): String =
    buildString {
        append("<!DOCTYPE html>")
        appendHTML(prettyPrint = false).dashboardHtmlDocument {
            head {
                meta(charset = "utf-8")
                meta(name = "viewport", content = "width=device-width, initial-scale=1")
                title { +"Mosaik UI · Dashboard 01" }
                link(rel = "stylesheet", href = "/static/output.css")
                script(src = "https://unpkg.com/alpinejs@3.14.7/dist/cdn.min.js") { defer = true }
            }
            body(classes = "min-h-screen bg-base-200 text-base-content") {
                attributes["data-block-preview"] = "dashboard-01"
                dashboard01Preview()
            }
        }
    }

private fun FlowContent.dashboard01Preview() {
    mDrawer(toggleId = "dashboard-01-drawer", openFrom = DrawerBreakpoint.Lg, classes = "min-h-screen") {
        mDrawerContent(classes = "min-h-screen") {
            dashboardHeader()
            main(classes = "p-4 lg:p-8 space-y-6") {
                dashboardHero()
                dashboardStats()
                dashboardMainGrid()
            }
        }
        mDrawerSide(classes = "bg-base-100 border-r border-base-300 min-h-full w-72") {
            dashboardSidebar()
        }
    }
}

private fun FlowContent.dashboardHeader() {
    header(
        classes =
            "sticky top-0 z-20 flex items-center justify-between border-b border-base-300 " +
                "bg-base-100/90 px-4 py-3 backdrop-blur lg:px-8",
    ) {
        div(classes = "flex items-center gap-3") {
            mDrawerButton(
                toggleId = "dashboard-01-drawer",
                variant = ButtonVariant.Default,
                style = ButtonStyle.Ghost,
                size = Size.Sm,
                classes = "lg:hidden",
            ) { +"Menu" }
            div {
                p(classes = "text-sm text-base-content/60") { +"Overview" }
                h1(classes = "text-xl font-semibold") { +"Analytics Dashboard" }
            }
        }
        div(classes = "flex items-center gap-3") {
            mButton(variant = ButtonVariant.Primary, size = Size.Sm) { +"Export" }
            mDropdown(alignment = DropdownAlignment.End) {
                mDropdownTrigger(classes = "rounded-full") {
                    mAvatar(classes = "ring ring-primary ring-offset-2 ring-offset-base-100") {
                        div(
                            classes =
                                "w-10 rounded-full bg-primary text-primary-content " +
                                    "grid place-items-center font-semibold",
                        ) { +"JW" }
                    }
                }
                mDropdownContent(classes = "mt-3 w-52 rounded-xl bg-base-100 p-2 shadow") {
                    mMenuTitle { +"jwillert" }
                    mMenuItem("#profile") { +"Profile" }
                    mMenuItem("#settings") { +"Settings" }
                    mMenuItem("#logout") { +"Sign out" }
                }
            }
        }
    }
}

private fun FlowContent.dashboardSidebar() {
    aside(classes = "flex min-h-full flex-col gap-6 p-4") {
        a(href = DASHBOARD_01_PREVIEW_PATH, classes = "flex items-center gap-3 px-2") {
            img(src = "/static/mosaik-logo.svg", alt = "Mosaik UI", classes = "h-8 w-8")
            div {
                p(classes = "text-sm text-base-content/60") { +"Mosaik" }
                p(classes = "font-semibold") { +"Dashboard 01" }
            }
        }
        mMenu(classes = "w-full") {
            mMenuTitle { +"Workspace" }
            mMenuItem("#overview", active = true) { +"Overview" }
            mMenuItem("#orders") { +"Orders" }
            mMenuItem("#customers") { +"Customers" }
            mMenuItem("#reports") { +"Reports" }
            mMenuTitle { +"Admin" }
            mMenuItem("#billing") { +"Billing" }
            mMenuItem("#team") { +"Team" }
        }
        mDivider(classes = "my-0") { +"Plan" }
        mCard(classes = "bg-primary text-primary-content shadow-sm") {
            mCardBody(classes = "p-4") {
                mCardTitle(classes = "text-base") { +"Pro workspace" }
                p(classes = "text-sm opacity-80") { +"Your team has used 78% of monthly reports." }
                mCardActions(classes = "justify-end") {
                    mButtonLink(href = "#upgrade", variant = ButtonVariant.Secondary, size = Size.Sm) { +"Upgrade" }
                }
            }
        }
    }
}

private fun FlowContent.dashboardHero() {
    section(classes = "grid gap-4 lg:grid-cols-[1fr_auto] lg:items-center") {
        div {
            h2(classes = "text-3xl font-bold tracking-tight") { +"Good morning, Jamie" }
            p(
                classes = "mt-2 text-base-content/70",
            ) { +"Track revenue, orders, and customer activity from a responsive shell." }
        }
        mJoin(classes = "justify-self-start lg:justify-self-end") {
            listOf("Today", "7d", "30d").forEachIndexed { index, label ->
                mJoinItem {
                    mButton(
                        variant = if (index == 1) ButtonVariant.Primary else ButtonVariant.Default,
                        size = Size.Sm,
                        classes = "rounded-none",
                    ) { +label }
                }
            }
        }
    }
}

private fun FlowContent.dashboardStats() {
    mStats(classes = "w-full shadow-sm") {
        mStat {
            mStatTitle { +"Revenue" }
            mStatValue { +"$48.2k" }
            mStatDesc(classes = "text-success") { +"↗︎ 12% from last month" }
        }
        mStat {
            mStatTitle { +"Orders" }
            mStatValue { +"1,284" }
            mStatDesc { +"82 awaiting fulfillment" }
        }
        mStat {
            mStatTitle { +"Conversion" }
            mStatValue { +"7.8%" }
            mStatDesc(classes = "text-warning") { +"Needs attention" }
        }
    }
}

private fun FlowContent.dashboardMainGrid() {
    section(classes = "grid gap-6 xl:grid-cols-[minmax(0,2fr)_minmax(22rem,1fr)]") {
        mCard(classes = "bg-base-100 shadow-sm") {
            mCardBody {
                div(classes = "flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between") {
                    div {
                        mCardTitle { +"Recent orders" }
                        p(
                            classes = "text-sm text-base-content/60",
                        ) { +"A static snapshot for the standalone block preview." }
                    }
                    div(classes = "flex items-center gap-2") {
                        mCheckbox(variant = CheckboxVariant.Primary, size = Size.Sm)
                        mButton(
                            variant = ButtonVariant.Default,
                            style = ButtonStyle.Outline,
                            size = Size.Sm,
                        ) { +"View all" }
                    }
                }
                div(classes = "overflow-x-auto") { ordersTable() }
            }
        }
        div(classes = "space-y-6") {
            activityCard()
            notesCard()
        }
    }
}

private fun FlowContent.ordersTable() {
    mTable(zebra = true, size = TableSize.Sm) {
        thead {
            tr {
                th { +"Customer" }
                th { +"Status" }
                th { +"Total" }
            }
        }
        tbody {
            listOf(
                OrderRow("Ada Lovelace", "Paid", "$320.00"),
                OrderRow("Grace Hopper", "Pending", "$184.00"),
                OrderRow("Katherine Johnson", "Paid", "$512.00"),
            ).forEach { order ->
                tr {
                    td { +order.customer }
                    td { +order.status }
                    td { +order.total }
                }
            }
        }
    }
}

private fun FlowContent.activityCard() {
    mCard(classes = "bg-base-100 shadow-sm") {
        mCardBody {
            mCardTitle { +"Team activity" }
            ul(classes = "space-y-4") {
                listOf(
                    "Deployed dashboard shell",
                    "Reviewed order exports",
                    "Invited finance team",
                ).forEach { item ->
                    li(classes = "flex items-center gap-3") {
                        mAvatar(placeholder = true) {
                            div(
                                classes =
                                    "w-8 rounded-full bg-secondary text-secondary-content " +
                                        "grid place-items-center text-xs",
                            ) {
                                +item.first().toString()
                            }
                        }
                        p(classes = "text-sm") { +item }
                    }
                }
            }
        }
    }
}

private fun FlowContent.notesCard() {
    mCard(classes = "bg-base-100 shadow-sm") {
        mCardBody {
            mCardTitle { +"Notes" }
            div(classes = "min-h-28 rounded-lg border border-base-300 bg-base-200 p-4 text-sm text-base-content/60") {
                +"Add a handoff note for the next shift"
            }
            mButton(variant = ButtonVariant.Primary, width = ButtonWidth.Block) { +"Save note" }
        }
    }
}

private data class OrderRow(
    val customer: String,
    val status: String,
    val total: String,
)
