<script lang="ts">
    import ChevronLeft from '../icons/ChevronLeft.svelte';
    import ChevronRight from '../icons/ChevronRight.svelte';

    export let title: String;
    export let position: "left" | "right";

    export let isVisible = true;
</script>

<div class="fixed inset-y-12 flex h-full {position == "right" ? "right-0" : ""}">
    <div class="relative {isVisible ? "w-screen" : "w-10"} max-w-xs">
        <div class="h-full flex flex-col {position == "right" ? "border-l" : "border-r"} bg-white border-gray-300">
            {#if isVisible}
                <div class="flex px-4 py-2 justify-between border-b border-gray-300">
                    {#if position == 'right'}
                        <button type="button" on:click={() => isVisible = false}>
                            <ChevronRight/>
                        </button>
                    {/if}
                    {title}
                    {#if position == 'left'}
                        <button type="button" on:click={() => isVisible = false}>
                            <ChevronLeft/>
                        </button>
                    {/if}
                </div>
                <slot></slot>
            {:else}
                <button class="px-2 py-3" type="button" on:click={() => isVisible = true}>
                    {#if position == 'left'}
                        <ChevronRight/>
                    {:else}
                        <ChevronLeft/>
                    {/if}
                </button>
                <div class="transform mt-2 px-2 rotate-90">{title}</div>
            {/if}
        </div>
    </div>
</div>