// Simple test script for the chatbot API
const axios = require('axios');

const API_BASE_URL = 'http://localhost:8080/api';

async function testChatbot() {
  try {
    console.log('Testing SmartCart Chatbot API...\n');
    
    // Test 1: Greeting
    console.log('Test 1: Greeting');
    const greetingResponse = await axios.post(`${API_BASE_URL}/chat`, {
      conversationId: 'test-conv-1',
      message: 'Hello'
    });
    console.log('Response:', greetingResponse.data.reply);
    console.log('Products count:', greetingResponse.data.products.length);
    console.log('---\n');
    
    // Test 2: Category search
    console.log('Test 2: Category search - Electronics');
    const categoryResponse = await axios.post(`${API_BASE_URL}/chat`, {
      conversationId: 'test-conv-1',
      message: 'Show me electronics'
    });
    console.log('Response:', categoryResponse.data.reply);
    console.log('Products count:', categoryResponse.data.products.length);
    if (categoryResponse.data.products.length > 0) {
      console.log('First product:', categoryResponse.data.products[0].title);
    }
    console.log('---\n');
    
    // Test 3: Price range search
    console.log('Test 3: Price range search - under 20k');
    const priceResponse = await axios.post(`${API_BASE_URL}/chat`, {
      conversationId: 'test-conv-1',
      message: 'Show me phones under 20k'
    });
    console.log('Response:', priceResponse.data.reply);
    console.log('Products count:', priceResponse.data.products.length);
    if (priceResponse.data.products.length > 0) {
      console.log('Products found:');
      priceResponse.data.products.forEach(product => {
        console.log(`- ${product.title}: ₹${product.price}`);
      });
    }
    console.log('---\n');
    
    // Test 4: Category + Price range
    console.log('Test 4: Category + Price range - Electronics under 1000');
    const combinedResponse = await axios.post(`${API_BASE_URL}/chat`, {
      conversationId: 'test-conv-1',
      message: 'Show me electronics under 1000'
    });
    console.log('Response:', combinedResponse.data.reply);
    console.log('Products count:', combinedResponse.data.products.length);
    console.log('---\n');
    
    // Test 5: Help request
    console.log('Test 5: Help request');
    const helpResponse = await axios.post(`${API_BASE_URL}/chat`, {
      conversationId: 'test-conv-1',
      message: 'What can you help me with?'
    });
    console.log('Response:', helpResponse.data.reply);
    console.log('---\n');
    
    console.log('All tests completed successfully! ✅');
    
  } catch (error) {
    console.error('Test failed:', error.response?.data || error.message);
  }
}

// Run the test
testChatbot();

