<template>
  <v-container>
    <v-row>
      <v-col>
        <v-card>
          <v-card-title class="text-center text-h5">
            채팅방목록
            <div class="d-flex justify-end">
              <v-btn color="secondary" @click="showCreateRoomModal = true">
                채팅방 생성
              </v-btn>
            </div>
          </v-card-title>
          <v-card-title>
            <v-table>
              <thead>
              <tr>
                <th>방번호</th>
                <th>방제목</th>
                <th>채팅</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="chat in chatRoomList" :key="chat.roomId">
                <td>{{ chat.roomId }}</td>
                <td>{{ chat.roomName }}</td>
                <td>
                  <v-btn color="primary" @click="joinChatRoom(chat.roomId)">참여하기</v-btn>
                </td>
              </tr>
              </tbody>
            </v-table>
          </v-card-title>
        </v-card>
      </v-col>
    </v-row>

    <v-dialog v-model="showCreateRoomModal" max-width="500px">
      <v-card>
        <v-card-title class="text-h6">
          채팅방 생성
        </v-card-title>
        <v-card-text>
          <v-text-field label="방제목" v-model="newRoomTitle"/>
        </v-card-text>
        <v-card-actions>
          <v-btn color="grey">취소</v-btn>
          <v-btn color="primary" @click="createChatRoom">생성</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </v-container>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      showCreateRoomModal: false,
      newRoomTitle: '',
      chatRoomList: [],
    }
  },
  created() {
    this.loadChatRoom();
  }, methods: {
    async joinChatRoom(roomId) {
      await axios.post(`${import.meta.env.VITE_API_BASE_URL}/chat/room/group/${roomId}/join`, null)
      this.$router.push({path: `/chat/page/${roomId}`});
    },
    async createChatRoom() {
      await axios.post(`${import.meta.env.VITE_API_BASE_URL}/chat/room/group/create?roomName=${this.newRoomTitle}`, null)
      this.showCreateRoomModal = false
      await this.loadChatRoom();
    },
    async loadChatRoom(){
      axios.get(`${import.meta.env.VITE_API_BASE_URL}/chat/room/group/list`).then(res => {
        this.chatRoomList = res.data;
      })
    }
  }
}
</script>















