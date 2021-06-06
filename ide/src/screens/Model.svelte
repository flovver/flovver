<script lang="ts">
    import SideBar from "../layout/SideBar.svelte";
    import TypeSet from "./model/type-set/TypeSet.svelte";
    import TypeSetItem from "./model/type-set/TypeSetItem.svelte";
    import { typeList, typesByName } from "./model/types/types";
    import Workspace from "./model/Workspace.svelte";
    import Properties from "./model/Properties.svelte";

    export let model;

    let types = model.types.map((v, i, a) =>
        Object.create({
            name: v.alias,
            baseType: v.base,
            x: v.x,
            y: v.y,
            component: typesByName[v.base],
            deleteAction: (i) => {
                types.splice(i, 1);
                types = types;
                setCurrentType(null);
            },
            model: model["model-type"] == v.alias,
            setModel: (i) => {
                types.forEach((v, i, a) => (v.model = false));
                types[i].model = true;
            },
            message: model["message-type"] == v.alias,
            setMessage: (i) => {
                types.forEach((v, i, a) => (v.message = false));
                types[i].message = true;
            },
            variants: v?.variants?.map((vv, ii, aa) =>
                Object.create({ name: vv.name, baseType: vv.type })
            ),
        })
    );

    let currentType;
    let setCurrentType;

    export let currentScreen;
</script>

<div class={currentScreen == "model" ? "visible" : "invisible"}>
    <Workspace bind:types bind:currentType bind:setCurrentType />

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
</div>
