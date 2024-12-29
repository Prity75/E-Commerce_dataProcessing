package com.ecommerce.config;

import com.ecommerce.model.Order;
import com.ecommerce.repository.OrderRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    // Reader to read the CSV file and map it to Order objects
    @Bean
    public FlatFileItemReader<Order> reader() {
        FlatFileItemReader<Order> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("E-commerce Dataset.csv"));  // The CSV file

        // Configure the CSV file structure (mapping CSV columns to Order fields)
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("orderDate", "customerId", "quantity", "product", "price", "profit", "paymentMethod", "shippingCost", "discount", "totalPrice");

        BeanWrapperFieldSetMapper<Order> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Order.class);

        DefaultLineMapper<Order> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        reader.setLineMapper(lineMapper);

        return reader;
    }

    // Writer to write the processed data to the database using OrderRepository
    @Bean
    public ItemWriter<Order> writer(OrderRepository orderRepository) {
        return new ItemWriter<Order>() {
            @Override
            public void write(Chunk<? extends Order> chunk) throws Exception {
                orderRepository.saveAll(chunk);  // Save all orders to the database
            }
        };
    }

    // Step definition to read from the file and write to the database
    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, FlatFileItemReader<Order> reader, ItemWriter<Order> writer) {
        return stepBuilderFactory.get("step1")
                .<Order, Order>chunk(10)  // Process 10 items at a time
                .reader(reader)
                .writer(writer)
                .build();
    }

    // Job definition to start the batch process
    @Bean
    public Job importUserJob(JobBuilderFactory jobBuilderFactory, Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .start(step1)  // Start with step1
                .build();
    }
}

