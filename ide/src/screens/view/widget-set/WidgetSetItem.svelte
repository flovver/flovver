<script lang="ts">
    export let title: string;
</script>

<div draggable=true class="flex m-2 px-2 py-1 items-center cursor-move hover:ring ring-black rounded shadow bg-gray-50">
    <div class="inline-block mr-4">
        <slot name="icon"></slot>
    </div>
    <span class="inline-block">{title}</span>
</div>