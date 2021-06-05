<script lang="ts">
    import { connectionMessages } from "../stores";
    import { onDestroy } from "svelte";

    export let x: number;
    export let y: number;

    const p = { x: x, y: y };

    $: {
        refreshSource(x, y);
        refreshDestination(x, y);
    }

    export let type: "input" | "output" = "input";
    export let position: "internal" | "external" = "external";

    export let hideable: boolean = false;

    let hidden = hideable;

    function onMouseDown(e: MouseEvent) {
        if (type == "output" || (type == "input" && position == "internal")) {
            refreshSource = setSource(p);
        }
    }

    function onMouseUp(e: MouseEvent) {
        if (type == "input" || (type == "output" && position == "internal")) {
            const c: any[] = addConnection(p);
            if (c) {
                [refreshDestination, deleteConnection] = c;
            }
        }
    }

    function onContextMenu(e: MouseEvent) {
        e.preventDefault();
        deleteConnection();
        return false;
    }

    function onMouseMove(e: MouseEvent) {}

    let setSource = (_) => (x, y) => {};
    let refreshSource = (x, y) => {};
    let addConnection = (_) => [(x, y) => {}, () => {}];
    let refreshDestination = (x, y) => {};
    let deleteConnection = () => {};

    const unsubscribe = connectionMessages.subscribe((v) => {
        setSource = v.setSource;
        addConnection = v.addConnection;
    });

    onDestroy(() => {
        deleteConnection();
        unsubscribe();
    });
</script>

<div
    class="fixed {hidden ? 'hover:' : ''}bg-gray-50 border-gray-400 {hidden
        ? 'hover:'
        : ''}border-2 hover:bg-gray-100 hover:border-gray-500 rounded-full w-3 h-3"
    style="left: {x}px; top: {y}px;"
    on:mousedown={onMouseDown}
    on:mouseup={onMouseUp}
    on:contextmenu={onContextMenu}
/>
