<script lang="ts">
    import SideBar from "../layout/SideBar.svelte";
    import TypeSet from "./model/type-set/TypeSet.svelte";
    import TypeSetItem from "./model/type-set/TypeSetItem.svelte";
    import { typeList } from "./model/types/types";
    import Workspace from "./model/Workspace.svelte";
    import Properties from "./model/Properties.svelte";

    let types = [];
    let currentType;
</script>

<Workspace bind:types bind:currentType />

<SideBar title="Types" position="left">
    <TypeSet>
        {#each typeList as t}
            <TypeSetItem title={t.title} />
        {/each}
    </TypeSet>
</SideBar>

<SideBar title="Properties" position="right">
    <Properties bind:types bind:currentType />
</SideBar>
