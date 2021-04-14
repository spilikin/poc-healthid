<template>
  <div>
    <v-card class="px-3 pb-6">
      <v-card-text>
        <h1>Choose HealthID Provider</h1>
        <v-list two-line>
          <v-list-item-group>
            <template v-for="(item, index) in identityProviderList">
              <v-list-item
                :key="item.id"
                @click="chooseIdentityProvider(item.id)"
              >
                <v-list-item-avatar color="white">
                  <v-icon color="blue">{{ item.logo_uri }}</v-icon>
                </v-list-item-avatar>

                <v-list-item-content>
                  <v-list-item-title v-html="item.title"></v-list-item-title>
                  <v-list-item-subtitle
                    v-html="item.subtitle"
                  ></v-list-item-subtitle>
                </v-list-item-content>

                <v-list-item-action>
                  <v-list-item-action-text>Select</v-list-item-action-text>
                  <v-icon color="grey" large> mdi-chevron-right </v-icon>
                </v-list-item-action>
              </v-list-item>

              <v-divider :key="index" />
            </template>
          </v-list-item-group>
        </v-list>
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from "vue-property-decorator";
import Buzzwords from "./BuzzwordsView.vue";
import { IdentityProvider, FederationQuery } from "./federation";

@Component({
  components: {
    Buzzwords,
  },
})
export default class ChooserView extends Vue {
  private identityProviderId!: string | null;

  set identityProviderModel(id: string | null) {
    localStorage.setItem("identityProviderId", id!);
    this.identityProviderId = id;
  }

  get identityProviderModel(): string | null {
    return this.identityProviderId;
  }

  chooseIdentityProvider(id: string) {
    this.identityProviderModel = id;
    this.$router.push("/login/auth");
  }

  created() {
    this.identityProviderId = localStorage.getItem("identityProviderId");
  }

  mounted() {
    console.log("Chooser View mounted");
    //localStorage.removeItem("identityProviderId")
  }

  get identityProviderList(): Array<IdentityProvider> {
    return new FederationQuery().identityProviderList;
  }
}
</script>

<style scoped>
#LoginSettings {
  transition: transform 0.3s ease-in-out;
}

#LoginSettings:hover {
  transform: rotate(120deg);
}

.separator {
  padding: 20px;
  margin-right: 10px;
  height: 40px;
  border-right: 1px white solid;
}
</style>
