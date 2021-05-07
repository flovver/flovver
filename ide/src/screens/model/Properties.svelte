<script lang="ts">
    import { builtinVariants } from "./types/types";
    import Trash from "../../icons/Trash.svelte";

    export let currentType;
    export let types: any[];

    let name;
    $: if (!currentType?.edit) name = currentType?.name;
    $: if (currentType?.edit && currentType.setName) {
        currentType.setName(name);
    }

    let variants: any[];
    $: if (!currentType?.edit) variants = currentType?.variant?.variants;
    $: if (currentType?.edit && currentType.variant) {
        currentType.variant.setVariants(variants);
    }

    function addVariant() {
        enableEdit();
        variants.push({ name: "", baseType: "Unit" });
        variants = variants;
    }

    function deleteVariant(i) {
        enableEdit();
        variants.splice(i, 1);
        variants = variants;
    }

    function enableEdit() {
        if (currentType) {
            currentType.edit = true;
        }
    }
</script>

{#if currentType}
    <div class="mt-4">
        <div class="flex px-4 justyify-center">
            <div class="mr-2 text-gray-600">type</div>
            <div>{currentType.baseType}</div>
        </div>
        <div class="flex px-4 mt-2 justyify-center">
            <div class="mr-2 text-gray-600">alias</div>
            <input
                bind:value={name}
                on:input={enableEdit}
                class="w-full"
                type="text"
            />
        </div>
        {#if currentType.variant}
            <div class="mt-8">
                <div class="px-4 font-medium text-gray-900">Variants</div>
                {#each variants as v, i}
                    <div class="flex p-4 justyify-center">
                        <div class="mr-2 font-medium">{i + 1}</div>
                        <div class="mr-2 text-gray-600">name</div>
                        <input
                            on:input={enableEdit}
                            bind:value={v.name}
                            class="w-full mr-2"
                            type="text"
                        />
                        <div class="mr-2 text-gray-600">type</div>
                        <select
                            on:change={enableEdit}
                            bind:value={v.baseType}
                            class="w-full bg-transparent"
                        >
                            {#each builtinVariants as t}
                                <option value={t}>{t}</option>
                            {/each}
                            {#each types as t}
                                {#if t.name != name}
                                    <option value={t.name}>{t.name}</option>
                                {/if}
                            {/each}
                        </select>
                        <button
                            on:click={() => deleteVariant(i)}
                            class="ml-2 text-gray-500 hover:text-gray-600"
                            ><Trash /></button
                        >
                    </div>
                {/each}
                <div class="px-4">
                    <button
                        on:click={addVariant}
                        class="w-full text-center p-1 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
                        >+</button
                    >
                </div>
            </div>
        {/if}
        <div class="mt-8">
            <div class="px-4 font-medium text-gray-900">Role</div>
            <div class="flex gap-2 p-4">
                <button
                    type="submit"
                    class="py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
                >
                    Set as Model
                </button>
                {#if currentType.baseType == "Variant"}
                    <button
                        type="submit"
                        class="py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
                    >
                        Set as Message
                    </button>
                {/if}
            </div>
        </div>
        <div class="mt-4">
            <div class="px-4 font-medium text-gray-900">Extra</div>
            <div class="p-4">
                <button
                    on:click={currentType.deleteAction}
                    type="submit"
                    class="py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
                >
                    Delete component
                </button>
            </div>
        </div>
    </div>
{:else}
    <div class="p-4 text-center text-gray-600">
        Select type in workspace to view its properties here
    </div>
{/if}
