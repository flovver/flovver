<script lang="ts">
    import SideBar from "../layout/SideBar.svelte";
    import Workspace from "./update/Workspace.svelte";
    import DefineSet from "./update/defines/DefineSet.svelte";
    import DefineSetItem from "./update/defines/DefineSetItem.svelte";
    import { primitivesList, functions } from "./update/nodes/nodes";

    export let currentScreen;
</script>

<div class={currentScreen == "update" ? "visible" : "invisible"}>
    <Workspace />

    <SideBar title="Functions" position="left">
        <DefineSet>
            {#each primitivesList as l}
                <DefineSetItem title={l} />
            {/each}
        </DefineSet>
        <DefineSet>
            {#each Object.keys(functions) as l}
                <DefineSetItem title={l} />
            {/each}
        </DefineSet>
    </SideBar>
</div>
