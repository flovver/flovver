<script lang="ts">
    export let title: string;

    function onDragStart(e: DragEvent) {
        e.dataTransfer.setData("typeName", title);
    }
</script>

<div
    on:dragstart={onDragStart}
    draggable="true"
    class="flex m-2 px-2 py-1 items-center cursor-move hover:ring ring-black rounded shadow bg-gray-50"
>
    {title}
</div>
