<script lang="ts">
  export let id: string;
  export let title;
  export let description: string;
  export let checked: boolean;
  export let disabled: boolean = false;
</script>

<div class="flex items-start">
  <div class="flex items-center h-5">
    <input
      {disabled}
      bind:checked
      {id}
      name={id}
      type="checkbox"
      class="focus:ring-indigo-500 h-4 w-4 text-indigo-600 border-gray-300 rounded"
    />
  </div>
  <div class="ml-3 text-sm">
    <label for={id} class="font-medium text-gray-700">
      <slot {title} />
    </label>
    <p class="text-gray-500">{description}</p>
  </div>
</div>
