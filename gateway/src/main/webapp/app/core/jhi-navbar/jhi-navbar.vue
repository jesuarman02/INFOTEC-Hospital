<template>
  <div>
    <b-navbar data-cy="navbar-gob" toggleable="md" type="dark" class="header-gob">
      <a class="navbar-brand" href="https://www.gob.mx">
        <img :src="'/content/images/logo_blanco.svg'" width="128" height="48" alt="Página de inicio, Gobierno de México" />
      </a>
      <b-collapse is-nav id="header-gob-tabs">
        <b-navbar-nav class="ml-auto">
          <a class="nav-item nav-link" href="https://www.gob.mx/tramites" target="_self" title="Ir a trámites del gobierno">Trámites</a>
          <a class="nav-item nav-link" href="https://www.gob.mx/gobierno" target="_self" title="Ir a gobierno">Gobierno</a>
          <a class="nav-item nav-link" href="https://www.gob.mx/busqueda" target="_self" title="Haz búsquedas en gobierno">
            <font-awesome-icon icon="search" />
          </a>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>

    <b-navbar data-cy="navbar" toggleable="md" type="light" class="bg-white">
      
      <b-navbar-brand class="brand-infotec" b-link to="/">
        <img width="150" height="auto" :src="'/content/images/logo_infotec.png'" alt="INFOTEC" />
        </b-navbar-brand>

      <b-navbar-toggle
        right
        class="jh-navbar-toggler d-lg-none"
        href="javascript:void(0);"
        data-toggle="collapse"
        target="header-tabs"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <font-awesome-icon icon="bars" />
      </b-navbar-toggle>

      <b-collapse is-nav id="header-tabs">
        <b-navbar-nav class="ml-auto">
          <b-nav-item to="/" exact>
            <span>
              <font-awesome-icon icon="home" />
              <span v-text="t$('global.menu.home')"></span>
            </span>
          </b-nav-item>

          <b-nav-item to="/interfaz-pacientes" active-class="active" v-if="authenticated">
            <span class="d-flex align-items-center">
              <img src="/content/images/llave.svg" alt="Pacientes" style="width: 18px; height: 18px; margin-right: 5px;" />
              <span v-text="t$('global.menu.adminPacientes.main')"></span>
            </span>
          </b-nav-item>

          <b-nav-item to="/ehr" exact v-if="authenticated">
            <span>
              <font-awesome-icon icon="users" />
              <!-- <span v-text="t$('global.menu.electronic-health-record')">Records</span> -->
            </span>
          </b-nav-item>
          
          <b-nav-item-dropdown right id="staff-menu" v-if="authenticated" active-class="active" class="pointer">
            <template #button-content>
              <span class="navbar-dropdown-menu">
                <font-awesome-icon icon="user-nurse" />
                <!-- <span class="no-bold" v-text="t$('global.menu.medical-staff')">Staff</span> -->
              </span>
            </template>
            </b-nav-item-dropdown>

          <b-nav-item-dropdown right id="entity-menu" v-if="authenticated" active-class="active" class="pointer" data-cy="entity">
            <template #button-content>
              <span class="navbar-dropdown-menu">
                <font-awesome-icon icon="th-list" />
                <span class="no-bold" v-text="t$('global.menu.entities.main')"></span>
              </span>
            </template>
            <entities-menu></entities-menu>
          </b-nav-item-dropdown>

          <b-nav-item-dropdown
            right
            id="admin-menu"
            v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated"
            :class="{ 'router-link-active': subIsActive('/admin') }"
            active-class="active"
            class="pointer"
            data-cy="adminMenu"
          >
            <template #button-content>
              <span class="navbar-dropdown-menu">
                <font-awesome-icon icon="users-cog" />
                <span class="no-bold" v-text="t$('global.menu.admin.main')"></span>
              </span>
            </template>
            <b-dropdown-item to="/admin/gateway" active-class="active">
              <font-awesome-icon icon="road" />
              <span v-text="t$('global.menu.admin.gateway')"></span>
            </b-dropdown-item>
            <b-dropdown-item to="/admin/user-management" active-class="active">
              <font-awesome-icon icon="users" />
              <span v-text="t$('global.menu.admin.userManagement')"></span>
            </b-dropdown-item>
            <b-dropdown-item to="/admin/metrics" active-class="active">
              <font-awesome-icon icon="tachometer-alt" />
              <span v-text="t$('global.menu.admin.metrics')"></span>
            </b-dropdown-item>
            <b-dropdown-item to="/admin/health" active-class="active">
              <font-awesome-icon icon="heart" />
              <span v-text="t$('global.menu.admin.health')"></span>
            </b-dropdown-item>
            <b-dropdown-item to="/admin/configuration" active-class="active">
              <font-awesome-icon icon="cogs" />
              <span v-text="t$('global.menu.admin.configuration')"></span>
            </b-dropdown-item>
            <b-dropdown-item to="/admin/logs" active-class="active">
              <font-awesome-icon icon="tasks" />
              <span v-text="t$('global.menu.admin.logs')"></span>
            </b-dropdown-item>
            <b-dropdown-item v-if="openAPIEnabled" to="/admin/docs" active-class="active">
              <font-awesome-icon icon="book" />
              <span v-text="t$('global.menu.admin.apidocs')"></span>
            </b-dropdown-item>
          </b-nav-item-dropdown>

          <b-nav-item-dropdown id="languagesnavBarDropdown" right v-if="languages && Object.keys(languages).length > 1">
            <template #button-content>
              <font-awesome-icon icon="flag" />
              <span class="no-bold" v-text="t$('global.menu.language')"></span>
            </template>
            <b-dropdown-item
              v-for="(value, key) in languages"
              :key="`lang-${key}`"
              @click="changeLanguage(key)"
              :class="{ active: isActiveLanguage(key) }"
            >
              {{ value.name }}
            </b-dropdown-item>
          </b-nav-item-dropdown>

          <b-nav-item-dropdown
            right
            href="javascript:void(0);"
            id="account-menu"
            :class="{ 'router-link-active': subIsActive('/account') }"
            active-class="active"
            class="pointer"
            data-cy="accountMenu"
          >
            <template #button-content>
              <span class="navbar-dropdown-menu">
                <font-awesome-icon icon="user" />
                <span class="no-bold" v-text="t$('global.menu.account.main')"></span>
              </span>
            </template>
            <b-dropdown-item data-cy="settings" to="/account/settings" v-if="authenticated" active-class="active">
              <font-awesome-icon icon="wrench" />
              <span v-text="t$('global.menu.account.settings')"></span>
            </b-dropdown-item>
            <b-dropdown-item data-cy="passwordItem" to="/account/password" v-if="authenticated" active-class="active">
              <font-awesome-icon icon="lock" />
              <span v-text="t$('global.menu.account.password')"></span>
            </b-dropdown-item>
            <b-dropdown-item data-cy="logout" v-if="authenticated" @click="logout()" id="logout" active-class="active">
              <font-awesome-icon icon="sign-out-alt" />
              <span v-text="t$('global.menu.account.logout')"></span>
            </b-dropdown-item>
            <b-dropdown-item data-cy="login" v-if="!authenticated" @click="openLogin()" id="login" active-class="active">
              <font-awesome-icon icon="sign-in-alt" />
              <span v-text="t$('global.menu.account.login')"></span>
            </b-dropdown-item>
            <b-dropdown-item data-cy="register" to="/register" id="register" v-if="!authenticated" active-class="active">
              <font-awesome-icon icon="user-plus" />
              <span v-text="t$('global.menu.account.register')"></span>
            </b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </div>
</template>

<script lang="ts" src="./jhi-navbar.component.ts"></script>

<style scoped>
/* ==========================================================================
    ESTILOS FUSIONADOS
   ========================================================================== */

/* --- Estilos de la Barra Gobierno (Nuevos) --- */
.header-gob {
  border-color: #fff;
  background-color: #611232; /* Color Vino Gobierno */
  min-height: 60px;
}

/* Enlaces del gobierno */
.header-gob .nav-link {
  color: #fff !important;
  font-weight: 300;
  font-size: 14px;
}

/* --- Estilos de la Barra Principal (JHipster + Infotec) --- */
.navbar-version {
  font-size: 0.65em;
}

/* Ajustes para el logo de Infotec */
.brand-infotec img {
  max-height: 45px;
  width: auto;
  margin-right: 10px;
}

/* Mantener el diseño responsivo */
@media screen and (min-width: 768px) {
  .jh-navbar-toggler {
    display: none;
  }
}

@media screen and (min-width: 768px) and (max-width: 1150px) {
  span span {
    display: none;
  }
}

.navbar-title {
  display: inline-block;
}

/* --- Ajuste global de links de navegación (Viene del código nuevo) --- */
/* Nota: Ajusté el padding-top para que no desalinee tus iconos actuales */
.navbar-expand-md .navbar-nav .nav-link {
  padding-left: 1rem;
  padding-right: 1rem;
  /* padding-top: 1.8rem;  <-- Comentado porque desalinea tu botón de Pacientes */
  display: flex;
  align-items: center;
}
/* Forzar color negro a los enlaces de la barra blanca */
.bg-white .navbar-nav .nav-link {
  color: #000000 !important; /* Negro puro */
}

/* Color al pasar el mouse (Gris oscuro) */
.bg-white .navbar-nav .nav-link:hover {
  color: #555555 !important;
}

/* Asegurar que los iconos de FontAwesome dentro de los links también sean negros */
.bg-white .navbar-nav .nav-link svg {
  color: #000000 !important;
}
</style>