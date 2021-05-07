<script lang="ts">
    import { typeList } from "./types/types";
    import Trash from "../../icons/Trash.svelte";

    export let currentType = 1;
</script>

{#if currentType}
    <div class="mt-4">
        <div class="flex px-4 justyify-center">
            <div class="mr-2 text-gray-600">type</div>
            <div>Int</div>
        </div>
        <div class="flex px-4 mt-2 justyify-center">
            <div class="mr-2 text-gray-600">alias</div>
            <input value={"Foo"} class="w-full" type="text" />
        </div>
        <div class="mt-8">
            <div class="px-4 font-medium text-gray-900">Variants</div>
            <div class="flex p-4 justyify-center">
                <div class="mr-2 font-medium">1</div>
                <div class="mr-2 text-gray-600">name</div>
                <input class="w-full mr-2" type="text" />
                <div class="mr-2 text-gray-600">type</div>
                <select class="w-full bg-transparent">
                    {#each typeList as t}
                        <option value={t.title}>{t.title}</option>
                    {/each}
                </select>
                <button class="ml-2 text-gray-500 hover:text-gray-600"
                    ><Trash /></button
                >
            </div>
            <div class="px-4">
                <button
                    class="w-full text-center p-1 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
                    >+</button
                >
            </div>
        </div>
        <div class="mt-8">
            <div class="px-4 font-medium text-gray-900">Role</div>
            <div class="flex gap-2 p-4">
                <button
                    type="submit"
                    class="w-full py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
                >
                    Set as Model
                </button>
                <button
                    type="submit"
                    class="w-full py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
                >
                    Set as Message
                </button>
            </div>
        </div>
        <div class="mt-4">
            <div class="px-4 font-medium text-gray-900">Extra</div>
            <div class="p-4">
                <button
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
