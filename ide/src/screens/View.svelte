<script lang="ts">
    import SideBar from "../layout/SideBar.svelte";
    import { widgetList } from "./view/widgets/widgets";
    import WidgetSet from "./view/widget-set/WidgetSet.svelte";
    import WidgetSetItem from "./view/widget-set/WidgetSetItem.svelte";
    import Workspace from "./view/Workspace.svelte";
    import Properties from "./view/Properties.svelte";

    let currentWidget;
</script>

<Workspace bind:currentWidget />

<SideBar title="Widgets" position="left">
    <WidgetSet>
        {#each widgetList as widget}
            <WidgetSetItem title={widget.title}>
                <svelte:component this={widget.icon} slot="icon" />
            </WidgetSetItem>
        {/each}
    </WidgetSet>
</SideBar>

<SideBar title="Properties" position="right">
    <Properties bind:currentWidget />
</SideBar>
