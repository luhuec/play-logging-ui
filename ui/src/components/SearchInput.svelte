<script lang="ts">
  import { queryStore } from "../stores";

  let query: string = "";

  $: queryStore.set(query);
</script>

<style lang="scss">
  input {
    background-color: #ffffff00;
    color: #ecf0f1;
    border: 2px solid #ecf0f1;
    border-radius: 5px;
    padding: 10px 10px;

    &::placeholder {
      color: #ecf0f1;
      opacity: 0.5;
    }
  }
</style>

<div>
  <input bind:value={query} type="text" placeholder="Filter loggers" />
</div>
