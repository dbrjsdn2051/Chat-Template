<template>
  <v-container>
    <v-row>
      <v-col>
        <v-card>
          <v-card-title class="text-center text-h5">
            회원목록
          </v-card-title>
          <v-card-title>
            <v-table>
              <thead>
              <tr>
                <th>ID</th>
                <th>이름</th>
                <th>email</th>
                <th>채팅</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="member in memberList" :key="member.id">
                <td>{{ member.id }}</td>
                <td>{{ member.name }}</td>
                <td>{{ member.email }}</td>
                <td>
                  <v-btn color="primary" @click="startChat(member.id)">채팅하기</v-btn>
                </td>
              </tr>
              </tbody>
            </v-table>
          </v-card-title>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      memberList: []
    }
  },
  created() {
    axios.get(`${import.meta.env.VITE_API_BASE_URL}/member/list`).then(res => {
      this.memberList = res.data;
    })
  }, methods: {
    async startChat(memberId) {
      await axios.post(`${import.meta.env.VITE_API_BASE_URL}/chat/room/private/create?memberId=${memberId}`, null)
          .then(res => {
            const roomId = res.data
            this.$router.push({path: `/chat/page/${roomId}`});
          }).catch(err => {
            console.log(err);
          })
    }
  }
}
</script>